package sma.rhythmtapper.game;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import java.util.*;
import sma.rhythmtapper.*;
import sma.rhythmtapper.framework.*;
import sma.rhythmtapper.framework.Impl.*;
import sma.rhythmtapper.framework.Input.*;
import sma.rhythmtapper.game.models.*;
import sma.rhythmtapper.models.*;
import sma.rhythmtapper.game.NoteFile.*;

public class GameScreen extends Screen
{
    private static final String TAG = "GameScreenTag";
    private Queue<Ball> balls;
    int ballsCount = 0;
    int ballsTotalCount = 0;
    private Queue<Ball> totalBalls;

    enum GameState
	{
        Ready, Running, Paused, GameOver
	}

    // game and device
    private int _gameHeight;
    private int _gameWidth;
    private Random _rand;
    private Difficulty _difficulty;
    //private int _lives;
    private Vibrator _vibrator;
    private boolean _isEnding;

	NoteFile noteFile;
    // score
    //private int _score;
    //private int _multiplier;
    //private int _streak;
    //private int _combo;

    GameStatusBundle bundle;
    Deck deck;
    GameResult result = new GameResult();

    // tickers
    private int _tick;                          //Period : 100000 Unit : 0.01sec
    private int _doubleMultiplierTicker;
    private int _explosionTicker;
    private float _currentTime;                 //Unit : 1 sec
    private int _endTicker;

    // balls
    private List<List<Ball>> _balls = new ArrayList<List<Ball>>(5);
    private List<Ball> _balls1;
    private List<Ball> _balls2;
    private List<Ball> _balls3;
    private List<Ball> _balls4;
    private List<Ball> _balls5;

    private boolean _longNote1;
    private boolean _longNote2;
    private boolean _longNote3;
    private boolean _longNote4;
    private boolean _longNote5;

    //TODO: how to support slide note?

    // lane miss indicators
    private int[] _laneHitAlpha = new int[5];
    /*private int _laneHitAlpha1;
	 private int _laneHitAlpha2;
	 private int _laneHitAlpha3;
	 private int _laneHitAlpha4;
	 private int _laneHitAlpha5;*/

    // difficulty params
    private float _spawnInterval;
    private int _ballSpeed;
    //private final double _spawnChance_normal = 0.10; // TODO dynamic
    //private final double _spawnChance_LeftFlick = _spawnChance_normal + 0.03;
    //private final double _spawnChance_RightFlick = _spawnChance_LeftFlick + 0.03;
    /*private final double _spawnChance_oneup = _spawnChance_LeftFlick;// + 0.003;
	 private final double _spawnChance_multiplier = _spawnChance_oneup;// + 0.001;
	 private final double _spawnChance_speeder = _spawnChance_multiplier;// + 0.003;
	 private final double _spawnChance_bomb = _spawnChance_speeder;// + 0.0005;
	 private final double _spawnChance_skull = _spawnChance_bomb;// + 0.014;*/

    // audio
    private Music _currentTrack;

    // ui
    private Paint _paintScore;
    private Paint _paintScoreShadow;
    private Paint _paintGameover;
    private Paint _paintScoreS;
    private Paint _paintScoreA;
    private Paint _paintScoreB;
    private Paint _paintScoreC;
    private Paint _paintScoreD;


    // constants
    // how far the screen should scroll after the track ends
    private static final int END_TIME = 1800;
    // initial y coordinate of spawned balls
    public static int HITBOX_CENTER = 1760;
    private static int HITBOX_HEIGHT = 200;
    public static int BALL_INITIAL_Y = HITBOX_CENTER;
    // hitbox is the y-range within a ball can be hit by a press in its lane

    // if no ball is in the hitbox when pressed, remove the lowest ball in the
    // miss zone right above the hitbox (it still counts as a miss)
    private static int MISS_ZONE_HEIGHT = 150;
    private static final int MISS_FLASH_INITIAL_ALPHA = 50;
    private static final int DOUBLE_MULTIPLIER_TIME = 600;
    // explosion
    private static final int EXPLOSION_TOP = 600;
    private static final int EXPLOSION_TIME = 150;

    private GameState state = GameState.Ready;

    GameScreen(Game game, Difficulty difficulty)
	{
        super(game);

        _difficulty = difficulty;
        // init difficulty parameters
        _ballSpeed = ((RTGame)game).getBallspeed();//_difficulty.getBallSpeed();
		EnvVar.speed = _ballSpeed;
        noteFile=((RTGame)game).getNoteFile();
		balls = noteFile.getBalls();
		ballsTotalCount = balls.size();
		ballsCount = 0;
		totalBalls = new ArrayDeque<>(balls);
		for(Ball b:totalBalls)
        {
            b.OnGameStart();
        }
		_spawnInterval = _difficulty.getSpawnInterval();

        // Initialize game objects
        _gameHeight = game.getGraphics().getHeight();
        _gameWidth = game.getGraphics().getWidth();
        _vibrator = game.getVibrator();
        //_multiplier = 1;
        _doubleMultiplierTicker = 0;
        //_score = 0;
        //_streak = 0;
        _balls1 = new ArrayList<>();
        _balls2 = new ArrayList<>();
        _balls3 = new ArrayList<>();
        _balls4 = new ArrayList<>();
        _balls5 = new ArrayList<>();
        _balls.add(0, _balls1);
        _balls.add(1, _balls2);
        _balls.add(2, _balls3);
        _balls.add(3, _balls4);
        _balls.add(4, _balls5);

        _longNote1 = false;
        _longNote2 = false;
        _longNote3 = false;
        _longNote4 = false;
        _longNote5 = false;

        _rand = new Random();
        _tick = 0;
        _endTicker = END_TIME / _difficulty.getBallSpeed();
        _currentTime = 0f;
		EnvVar.currentTime = _currentTime;
        _explosionTicker = 0;
        ///_lives = 10;
        this.deck = ((RTGame)game).getDeck();
		
        deck.SetResult(result);
        this.bundle = new GameStatusBundle(deck);

        for (int i = 0; i < 5; i++)
		{
            _laneHitAlpha[i] = 0;
        }
		/*
		 _laneHitAlpha1 = 0;
		 _laneHitAlpha2 = 0;
		 _laneHitAlpha3 = 0;
		 _laneHitAlpha4 = 0;
		 _laneHitAlpha5 = 0;*/
        _currentTrack = Assets.musicTrack;
        _isEnding = false;

        // paints for text
        _paintScore = new Paint();
        _paintScore.setTextSize(60);
        _paintScore.setTextAlign(Paint.Align.CENTER);
        _paintScore.setAntiAlias(true);
        _paintScore.setColor(Color.RED);
        _paintScore.setStrokeWidth(8);

        // paints for text
        _paintScoreShadow = new Paint();
        _paintScoreShadow.setTextSize(60);
        _paintScoreShadow.setStrokeWidth(12);
        _paintScoreShadow.setStyle(Paint.Style.STROKE);
        _paintScoreShadow.setTextAlign(Paint.Align.CENTER);
        _paintScoreShadow.setAntiAlias(true);
        _paintScoreShadow.setColor(Color.WHITE);

        _paintGameover = new Paint();
        _paintGameover.setTextSize(60);
        _paintGameover.setTextAlign(Paint.Align.CENTER);
        _paintGameover.setAntiAlias(true);
        _paintGameover.setColor(Color.BLACK);

        _paintScoreS = new Paint();
        _paintScoreS.setAntiAlias(true);
        int[] colors = {Color.rgb(0xFF, 0x7F, 00), Color.YELLOW, Color.BLUE, Color.rgb(0xfa, 0xb7, 0xf7), Color.MAGENTA};
        float[] pos = {0.0f, 0.6f, 0.75f, 0.9f, 1.0f};
        _paintScoreS.setShader(new LinearGradient(_gameWidth * 0.5f, 0, _gameWidth, 0, colors, pos, Shader.TileMode.CLAMP));

        _paintScoreA = new Paint();
        _paintScoreA.setColor(Color.rgb(0xf8, 0x38, 0x25));

        _paintScoreB = new Paint();
        _paintScoreB.setColor(Color.rgb(0xff, 0xa5, 0x00));

        _paintScoreC = new Paint();
        _paintScoreC.setColor(Color.YELLOW);

        _paintScoreD = new Paint();
        _paintScoreD.setColor(Color.rgb(0xc6, 0x8a, 0x12));


        HITBOX_CENTER = game.getScreenY() - HITBOX_HEIGHT;
        BALL_INITIAL_Y = HITBOX_CENTER;
        EnvVar.sizeY = game.getScreenY();
        EnvVar.sizeX = game.getScreenX();
        EnvVar.gameWidth = _gameWidth;
        EnvVar.gameHeight = _gameHeight;
        EnvVar.HITBOX_CENTER = HITBOX_CENTER;

    }

    @Override
    public void update(float deltaTime)
	{
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        if (state == GameState.Ready)
            updateReady(touchEvents);
        if (state == GameState.Running)
            updateRunning(touchEvents, deltaTime);
        if (state == GameState.Paused)
            updatePaused(touchEvents);
        if (state == GameState.GameOver)
            updateGameOver(touchEvents);
    }

    private void updateReady(List<TouchEvent> touchEvents)
	{
        if (touchEvents.size() > 0)
		{
            state = GameState.Running;
            deck.StartGame(bundle);
            touchEvents.clear();
            _currentTrack.setLooping(false);
            _currentTrack.setVolume(0.25f);
            _currentTrack.play();
        }
    }

    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime)
	{
        deck.Update(deltaTime);
        // 1. All touch input is handled here:
        handleTouchEvents(touchEvents);

        // 2. Check miscellaneous events like death:
        checkDeath();
        checkEnd();

        // 3. Individual update() methods.
        updateVariables(deltaTime);
    }

    private void checkEnd()
	{
        if (ballsCount == ballsTotalCount)//_currentTrack.isStopped())
		{
            _isEnding = true;
        }
    }

    /*
    private void explosion(List<Ball> balls)
	{
        Iterator<Ball> iter = balls.iterator();
        while (iter.hasNext())
		{
            Ball b = iter.next();
            if (b.y > EXPLOSION_TOP)
			{
                iter.remove();
                bundle.testResult = TestResult.PERFECT;
                deck.Apply(bundle);
                //_score += 10 * _multiplier
                //        * (_doubleMultiplierTicker > 0 ? 2 : 1);
            }
        }
    }
*/
    private void OnBallFinished(Bundle bundle)
    {

    }
    private void checkDeath()
	{
        if (bundle.isDead())
		{
            endGame();
        }
    }

    private void endGame()
	{
        state = GameState.GameOver;
        // update highscore
        FileIO fileIO = game.getFileIO();
        SharedPreferences prefs = fileIO.getSharedPref();
        int oldScore;
        oldScore=prefs.getInt(_difficulty.getMode().name(),0);

        if (bundle.score > oldScore)
		{
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(_difficulty.getMode().name(), bundle.score);
            editor.apply();
            result.highscore = bundle.score;
        }
		else
		{
            result.highscore = oldScore;
        }
        game.setScreen(new ResultScreen(game, result));
    }

    //For flicks
    int flickStartX, flickStartY;
	List<Integer> flickStartXs=new ArrayList<>();
	List<Integer> flickStartYs=new ArrayList<>();
	List<Integer> trackXs=new ArrayList<>();
	List<Integer> trackYs=new ArrayList<>();
	List<Finger> fingers=new ArrayList<>();
    int oldX, oldY;
    //for slide notes
    int trackX, trackY;
    boolean isDown;

    private void handleTouchEvents(List<TouchEvent> touchEvents)
	{
        int len = touchEvents.size();

        for (int i = 0; i < len; i++)
		{
            TouchEvent event = touchEvents.get(i);
            //only check flick here
            if (event.type == TouchEvent.TOUCH_DRAGGED)
			{
				Finger finger=findFinger(event.pointer);
				if(finger==null){
					fingers.add(new Finger(event.x,event.y,event.pointer));
					return;
					//FIXME
				}
				finger.vx = event.x-finger.x;
				finger.vy = event.y-finger.y;
                //trackX = event.x;
                //trackY = event.y;
                //int dx = event.x - oldX;
                //int dy = event.y - oldY;
                //get the lanes between the two points: first and now
                //then later reset the first
                for (int j=0;j < 5;j++)
                {
					if(hitLane(_balls.get(j),Ball.BallType.Slide))
						break;
                    //int midline=_gameWidth / 10 * (2 * j - 1);
					int lline=_gameWidth/5*i;
					int rline= _gameWidth/5*(i+1);
                    /*int dsx= finger.x - midline;
                    int dnx=trackX - midline;*/
                   	if((event.x>lline&&event.x<rline)||(finger.x>lline&&finger.x<rline))
                    {
                        //flick occured
                        if (finger.vx<0)
                        {
                            //left flick
                            hitLane(_balls.get(j), Ball.BallType.FlickLeft);
                            //flickStartX = trackX;
                        }
						else if(finger.vx>0)
						{
                            hitLane(_balls.get(j), Ball.BallType.FlickRight);
                            //flickStartX = trackX;
                        }
                    }
                }
				finger.x=event.x;
				finger.y=event.y;
			
				//flickStartX=trackX;
                /*if(trackX)
				 if (dy > dx)//up or left
				 {
				 if (dy > -dx) {
				 //up flick
				 } else {
				 //left flick
				 if (flickStartX > event.x) {
				 int flickStartLane = getFlickStartLaneLeft(flickStartX);
				 int flickEndLane = getFlickEndLaneLeft(event.x);
				 for (int j = flickStartLane; j >= flickEndLane; j--) {
				 hitLane(_balls.get(j), Ball.BallType.FlickLeft);
				 }
				 }
				 }
				 } else { //down or right
				 if (dy > -dx) {
				 //right
				 if (flickStartX < event.x) {
				 int flickStartLane = getFlickStartLaneRight(flickStartX);
				 int flickEndLane = getFlickEndLaneRight(event.x);
				 for (int j = flickStartLane; j <= flickEndLane; j++) {
				 hitLane(_balls.get(j), Ball.BallType.FlickRight);
				 }
				 }
				 } else {
				 //down
				 }
				 }
				 */
            }
            if (event.type == TouchEvent.TOUCH_DOWN)
			{
                //oldX = event.x;
                //oldY = event.y;
                //trackX = event.x;
                //trackY = event.y;
                //flickStartY = event.x;
                //flickStartX = event.y;
				Finger finger=new Finger(event.x,event.y,event.pointer);
				fingers.add(finger);
                //isDown = true;
                if (event.y > game.getScreenY() * 0.5f)
				{
                    // ball hit area
                    for (int j = 0; j < 5; j++)
					{
                        if (event.x < _gameWidth / 5 * (j + 1))
						{
                            if (hitLane(_balls.get(j), Ball.BallType.Normal))
							{
                            }
							else if(hitLane(_balls.get(j), Ball.BallType.LongDown))
							{
								finger.shouldHold=true;
							}
                            break;
                        }
                    }
					
                }
				else
				{
                    // pause area
                    touchEvents.clear();
                    pause();
                    break;
                }
            }
			else if (event.type == TouchEvent.TOUCH_UP)
			{
				Finger finger=findFinger(event.pointer);
				if(finger==null)
				    return;
                //isDown = false;
				if (event.y > game.getScreenY() * 0.5f)
				{
                    // ball hit area
                    for (int j = 0; j < 5; j++)
					{
                        if (event.x < _gameWidth / 5 * (j + 1))
						{
                            if (!hitLane(_balls.get(j), Ball.BallType.LongUp)&&finger.shouldHold)
							{
							    Ball toremove = LowestBall(_balls.get(j));
							    RemoveBall(_balls.get(j),toremove);
								onMiss(toremove);
                                // if no ball was hit
                                //_laneHitAlpha[j] = MISS_FLASH_INITIAL_ALPHA;
                            }
                            break;
                        }
                    }

                }
				fingers.remove(finger);
            }
        }
    }
	public Finger findFinger(int x, int y)
	{
		for(Finger f: fingers)
		{
			if(f.x==x&&f.y==y)
			{
				return f;
			}
		}
		return null;
	}
	public Finger findFinger(int id)
	{
		for(Finger f: fingers)
		{
			if(f.pointerid==id)
			{
				return f;
			}
		}
		return null;
	}
    private int getFlickStartLaneLeft(int flickStartX)
	{
        //lane's middle line
        int div = _gameWidth / 10;
        int odd = flickStartX / div;
        boolean ok = odd % 2 == 1;
        if (ok)
            return odd / 2;
        else
            return odd / 2 - 1;
    }

    private int getFlickStartLaneRight(int flickStartX)
	{
        //lane's middle line
        int div = _gameWidth / 10;
        int odd = flickStartX / div;
        boolean ok = odd % 2 == 0;
        if (ok)
            return odd / 2;
        else
            return odd / 2 + 1;
    }

    private int getFlickEndLaneLeft(int flickEndX)
	{
        //lane's middle line
        int div = _gameWidth / 10;
        int odd = flickStartX / div;
        boolean ok = odd % 2 == 0;
        if (ok)
            return odd / 2;
        else
            return odd / 2 + 1;
    }

    private int getFlickEndLaneRight(int flickEndX)
	{
        //lane's middle line
        int div = _gameWidth / 10;
        int odd = flickStartX / div;
        boolean ok = odd % 2 == 1;
        if (ok)
            return odd / 2;
        else
            return odd / 2 - 1;
    }

    // update all the games variables each tick
    // Deltatime : 1/100s = 0.01s = 10ms
    // If deltatime == 100 : 1sec
    private void updateVariables(float deltatime)
	{
        // update timer
        _currentTime += deltatime * 0.01f;
		EnvVar.currentTime = _currentTime;
        // update ball position
        for(Ball b: totalBalls)
        {
            b.update((int)(_ballSpeed * deltatime));
        }
        /*for (Ball b : _balls1)
		{
            b.update((int) (_ballSpeed * deltatime));
        }

        for (Ball b : _balls2)
		{
            b.update((int) (_ballSpeed * deltatime));
        }

        for (Ball b : _balls3)
		{
            b.update((int) (_ballSpeed * deltatime));
        }

        for (Ball b : _balls4)
		{
            b.update((int) (_ballSpeed * deltatime));
        }

        for (Ball b : _balls5)
		{
            b.update((int) (_ballSpeed * deltatime));
        }
        */

        for (int i = 0; i < 5; i++)
		{
            if (removeMissed(_balls.get(i).iterator()))
			{
                _laneHitAlpha[i] = MISS_FLASH_INITIAL_ALPHA;
            }

        }
        // remove missed balls
		/*
		 if (removeMissed(_balls1.iterator()))
		 {
		 _laneHitAlpha1 = MISS_FLASH_INITIAL_ALPHA;
		 }

		 if (removeMissed(_balls2.iterator()))
		 {
		 _laneHitAlpha2 = MISS_FLASH_INITIAL_ALPHA;
		 }

		 if (removeMissed(_balls3.iterator()))
		 {
		 _laneHitAlpha3 = MISS_FLASH_INITIAL_ALPHA;
		 }

		 if (removeMissed(_balls4.iterator()))
		 {
		 _laneHitAlpha4 = MISS_FLASH_INITIAL_ALPHA;
		 }


		 if (removeMissed(_balls5.iterator()))
		 {
		 _laneHitAlpha5 = MISS_FLASH_INITIAL_ALPHA;
		 }
		 */
        // spawn new balls
        if (!_isEnding /*&& _currentTime % _spawnInterval <= deltatime*/)
		{
            spawnBalls();
        }

        // decrease miss flash intensities
        for (int i = 0; i < 5; i++)
		{
            _laneHitAlpha[i] -= Math.min(_laneHitAlpha[i], 10);
        }
		/*
		 _laneHitAlpha1 -= Math.min(_laneHitAlpha1, 10);
		 _laneHitAlpha2 -= Math.min(_laneHitAlpha2, 10);
		 _laneHitAlpha3 -= Math.min(_laneHitAlpha3, 10);
		 _laneHitAlpha4 -= Math.min(_laneHitAlpha4, 10);
		 _laneHitAlpha5 -= Math.min(_laneHitAlpha5, 10);
		 */
        // atom explosion ticker
        //if (_explosionTicker > 0)
		//{
        //    for (List<Ball> bals : _balls)
        //        explosion(bals);
			/* explosion(_balls1);
			 explosion(_balls2);
			 explosion(_balls3);
			 explosion(_balls4);
			 explosion(_balls5);*/
        //}

        // update tickers
        //_doubleMultiplierTicker -= Math.min(1, _doubleMultiplierTicker);
        //_explosionTicker -= Math.min(1, _explosionTicker);
        _tick = (_tick + 1) % 100000;

        if (_isEnding)
		{
            _endTicker -= Math.min(1, _endTicker);

            if (_endTicker <= 0)
			{
                endGame();
            }
        }
    }

    // remove the balls from an iterator that have fallen through the hitbox
    private boolean removeMissed(Iterator<Ball> iterator)
	{
        while (iterator.hasNext())
		{
            Ball b = iterator.next();
			if (b.isFlick())
			{
				if (b.y > HITBOX_CENTER + HITBOX_HEIGHT)
				{
				    removeBall(iterator,b);
					Log.d(TAG, "fail press");
					onMiss(b);

					return true;
				}
			}
			else
			{
				if (b.y > HITBOX_CENTER + HITBOX_HEIGHT / 2)
				{
					removeBall(iterator,b);
					Log.d(TAG, "fail press");
					onMiss(b);
					return b.type != Ball.BallType.Skull;
				}
			}
        }
        return false;
    }

	boolean isMiss(Ball b)
	{
		if(!b.isNormal())
		{
			return b.y>HITBOX_CENTER+HITBOX_HEIGHT;
		} else {
			return b.y>HITBOX_CENTER+HITBOX_HEIGHT/2;
		}
	}
	
	boolean isHitable(Ball b)
	{
		if(isFlick(b))
		{
			return b.y<HITBOX_CENTER+HITBOX_HEIGHT&&b.y>HITBOX_CENTER-HITBOX_HEIGHT;
		} else if (b.isLongNote())
		{
			return b.y<HITBOX_CENTER+HITBOX_HEIGHT*2&&b.y>HITBOX_CENTER-HITBOX_HEIGHT*2;
		}
		else{
			return b.y<HITBOX_CENTER+HITBOX_HEIGHT/2&&b.y>HITBOX_CENTER-HITBOX_HEIGHT/2; 
		}
	}
	Ball LowestBall(List<Ball> balls)
	{
		Iterator<Ball> iter = balls.iterator();
        Ball lowestBall = null;
        while (iter.hasNext())
		{
            Ball b = iter.next();
            if (lowestBall == null || b.y > lowestBall.y)
			{
                lowestBall = b;
            }
        }
		return lowestBall;
	}
    // handles a TouchEvent on a certain lane
    private boolean hitLane(List<Ball> balls, Ball.BallType type)
	{    
		Ball lowestBall=LowestBall(balls);
        if (lowestBall != null && isHitable(lowestBall))
		{
            if (!lowestBall.isNormal())
			{
                if ((lowestBall.flick==1 && type.equals(Ball.BallType.FlickLeft)) ||
					(lowestBall.flick==2 && type.equals(Ball.BallType.FlickRight))) //(lowestBall.type == type)
				{
				    RemoveBall(balls, lowestBall);
                    onHit(lowestBall);
					return true;
                } else if(lowestBall.isLongUp()&&type==Ball.BallType.LongUp){
					RemoveBall(balls, lowestBall);
					onHit(lowestBall);
					return true;
				} else if(lowestBall.isSlideNote() && type ==Ball.BallType.Slide){
					RemoveBall(balls, lowestBall);
					onHit(lowestBall);
					return true;// type==Ball.BallType.;
				} else if(lowestBall.isLongDown() && type==Ball.BallType.LongDown){
					RemoveBall(balls, lowestBall);
					onHit(lowestBall);
					return true;
				} else {
					return false;
				}
            }
			else if(type==Ball.BallType.Normal)
			{
			    RemoveBall(balls,lowestBall);
                balls.remove(lowestBall);
                onHit(lowestBall);
            } else{
				return false;
			}

            return lowestBall.type != Ball.BallType.Skull;
        }
		else
		{
            if (lowestBall != null && lowestBall.y > HITBOX_CENTER - HITBOX_HEIGHT / 2 - MISS_ZONE_HEIGHT)
			{
                //balls.remove(lowestBall);
            }
            //onMiss(null);//bad hit

            return false;
        }
    }

    private void removeBall(Iterator<Ball> iterator,Ball ball)
    {
        ball.alive = false;
        iterator.remove();
        RemoveHelper(ball);
        RemoveTail(ball);
    }


    private void RemoveBall(List<Ball> balls, Ball ball)
    {
        balls.remove(ball);
        ball.alive = false;
        RemoveHelper(ball);
        RemoveTail(ball);
    }

    private void RemoveTail(Ball ball) {
        Connector toremove=null;

        for(Connector tail: aliveTails)
        {
            if(tail.ball2.equals(ball)) {
                toremove = tail;
                break;
            }
        }
        if(toremove!=null)
        {
            Log.v(TAG, "REMOVE TAIL1");
            aliveTails.remove(toremove);
        }
    }

    private void RemoveHelper(Ball ball) {
        HelperLine toremoveH = null;
        for(HelperLine h:helperLines)
        {
            if (h.ball1.equals(ball) || h.ball2.equals(ball)) {
                toremoveH = h;
                break;
            }
        }
        if(toremoveH != null)
        {
            helperLines.remove(toremoveH);
        }
    }

    private void onMiss(Ball b)
	{
        if (b != null && b.type == Ball.BallType.Skull)
		{
            return;
        }
        //_vibrator.vibrate(100);
        bundle.testResult = TestResult.MISS;
        OnBallTestEnded(b);
        //_streak = 0;
        //_combo=0;
        //_score -= Math.min(_score, 50);
        //_multiplier = 1;
        //--_lives;
        //updateMultipliers();
    }

	static boolean isFlick(Ball b)
	{
		return b.flick!=0;//b.type==Ball.BallType.FlickLeft || b.type==Ball.BallType.FlickRight;
	}
    // triggers when a lane gets tapped that currently has a ball in its hitbox
    private void onHit(Ball b)
	{
        //_streak++;
        //++_lives;
        //++_combo;
        int y = b.y;
        //bad      10%  HITBox_center-Hitboxheight
        //nice     15%  hitboxcenter-hitboxheight*0.4/2
        //great    15%  hitboxcenter-hitboxheight*0.25/2
        //perfect  20%  hitboxcenter+-hitboxheight*0.15
        //great    15%
        //nice     15%
        //bad      10%
		if(b.isFlick())
			HITBOX_HEIGHT*=2;
		else if(b.isLongNote())
			HITBOX_HEIGHT*=4;
        int diff = Math.abs(HITBOX_CENTER - y);
        TestResult tr = TestResult.MISS;
        if (diff <= HITBOX_HEIGHT * 0.1)
		{
            tr = TestResult.PERFECT;
        }
		else if (diff <= HITBOX_HEIGHT * 0.25)
		{
            tr = TestResult.GREAT;
        }
		else if (diff <= HITBOX_HEIGHT * 0.4)
		{
            tr = TestResult.NICE;
        }
		else if (diff <= HITBOX_HEIGHT / 2)
		{
            tr = TestResult.BAD;
        }
		if(b.isFlick())
			HITBOX_HEIGHT/=2;
		else if(b.isLongNote())
			HITBOX_HEIGHT/=4;
        bundle.testResult = tr;
        OnBallTestEnded(b);
		/* switch(b.type) {
		 case OneUp: {
		 //++_lives;
		 } break;
		 case Multiplier: {
		 //_doubleMultiplierTicker = DOUBLE_MULTIPLIER_TIME;
		 } break;
		 case Bomb: {
		 //_explosionTicker = EXPLOSION_TIME;
		 Assets.soundExplosion.play(0.7f);
		 } break;
		 case Skull: {
		 //onMiss(null); // hitting a skull counts as a miss
		 Assets.soundCreepyLaugh.play(1);
		 return;
		 }
		 }*/

        //updateMultipliers();
        //_score += 10 * _multiplier*_combo;
        //* (_doubleMultiplierTicker > 0 ? 2 : 1);
    }

    private void OnBallTestEnded(Ball b) {
        deck.Apply(bundle);
        ballsCount ++;
        if (bundle.testResult.compareTo(TestResult.BAD) >= 0)
		{
            if (isFlick(b))
			{
                Assets.soundFlickOK.play(1);
            }
			else
			{
                Assets.soundClick.play(1);
            }
        }
		else
		{
            Assets.soundMiss.play(1);
        }
    }

    private void spawnBalls()
	{
        //final int ballY = BALL_INITIAL_Y;
        while(balls.peek()!=null)
        {
            Ball ball = balls.peek();
            if(ball.time*1000 - 4000 >_currentTrack.getCurrentPosition())
                break;
            //Log.v(TAG,"Spawn ball:"+ball.time+"s; current:"+_currentTime+"s; music:"+_currentTrack.);
            ball = balls.remove();
            //ball.OnGameStart();
            //int ballX = (int)(_gameWidth / 5 / 2 * (2.0 * ball.startLane - 1.0));
            spawnBall(_balls.get((int)(ball.endLine-1)),ball/*,ballY*/);
        }
        /*
        float randFloat = _rand.nextFloat();

        int ballX = _gameWidth / 5 / 2;
        spawnBall(_balls1, randFloat, ballX, ballY);

        randFloat = _rand.nextFloat();
        ballX = _gameWidth / 5 / 2 * 3;
        spawnBall(_balls2, randFloat, ballX, ballY);

        randFloat = _rand.nextFloat();
        ballX = _gameWidth / 2;
        spawnBall(_balls3, randFloat, ballX, ballY);

        randFloat = _rand.nextFloat();
        ballX = _gameWidth - _gameWidth / 5 / 2 * 3;
        spawnBall(_balls4, randFloat, ballX, ballY);

        randFloat = _rand.nextFloat();
        ballX = _gameWidth - _gameWidth / 5 / 2;
        spawnBall(_balls5, randFloat, ballX, ballY);
    */
    }

    private void spawnBall(List<Ball> lane, Ball ball/*,int ballY*/)
	{
	    //ball.x = ballX;
	    //ball.y = ballY;
	    lane.add(ball);
	    //get a ball from the balls
        //get all the balls that needs to be spawned now
        //frame
        // while(balls.peek().frame<=frame)
        // {
        //    Ball ball=balls.get();
        //    ball을 배정한다
        //    ball.endlane

	    /*
        if (randFloat < _spawnChance_normal)
		{
            balls.add(0, new Ball(ballX, ballY, Ball.BallType.Normal));
        }
		else if (randFloat < _spawnChance_LeftFlick)
		{
            balls.add(0, new Ball(ballX, ballY, Ball.BallType.FlickLeft));
        }
		else if (randFloat < _spawnChance_RightFlick)
		{
            balls.add(0, new Ball(ballX, ballY, Ball.BallType.FlickRight));
        }*/
		/*else if (randFloat < _spawnChance_speeder)
		 {
		 balls.add(0, new Ball(ballX, ballY, Ball.BallType.Speeder));
		 }
		 else if (randFloat < _spawnChance_bomb)
		 {
		 balls.add(0, new Ball(ballX, ballY, Ball.BallType.Bomb));
		 }
		 else if (randFloat < _spawnChance_skull)
		 {
		 balls.add(0, new Ball(ballX, ballY, Ball.BallType.Skull));
		 }*/
    }

    private void updatePaused(List<TouchEvent> touchEvents)
	{
        if (_currentTrack.isPlaying())
		{
            _currentTrack.pause();
        }

        int len = touchEvents.size();
        for (int i = 0; i < len; i++)
		{
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_DOWN)
			{
                resume();
                return;
            }
        }
    }

    private void updateGameOver(List<TouchEvent> touchEvents)
	{
        if (!_currentTrack.isStopped())
		{
            _currentTrack.stop();
        }

        int len = touchEvents.size();
        for (int i = 0; i < len; i++)
		{
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP)
			{
                if (event.x > 300 && event.x < 540 && event.y > 845
					&& event.y < 1100)
				{
                    game.goToActivity(MainActivity.class);
                    return;
                }
				else if (event.x >= 540 && event.x < 780 && event.y > 845
						 && event.y < 1100)
				{
                    game.setScreen(new LoadingScreen(game, _difficulty));
                }
            }
        }

    }

    @Override
    public void paint(float deltaTime)
	{
        Graphics g = game.getGraphics();

        // First draw the game elements.

        // Example:
        g.drawScaledImage(Assets.background, 0, 0, _gameWidth, _gameHeight, 0, 0, Assets.background.getWidth(), Assets.background.getHeight());
        //for (int i = 0; i < 5; i++)
        //    g.drawRect(_gameWidth / 5 * i, 0, _gameWidth / 5 + 1, _gameHeight, Color.argb(_laneHitAlpha[i], 255, 0, 0));
        /*g.drawRect(_gameWidth / 5    , 0, _gameWidth / 5 + 1, _gameHeight, Color.argb(_laneHitAlpha2, 255, 0, 0));
		 g.drawRect(_gameWidth / 5 * 2, 0, _gameWidth / 5 + 1, _gameHeight, Color.argb(_laneHitAlpha3, 255, 0, 0));
		 g.drawRect(_gameWidth / 5 * 3, 0, _gameWidth / 5 + 1, _gameHeight, Color.argb(_laneHitAlpha4, 255, 0, 0));
		 g.drawRect(_gameWidth / 5 * 4, 0, _gameWidth / 5 + 1, _gameHeight, Color.argb(_laneHitAlpha5, 255, 0, 0));
		 */
        final int dx = _gameWidth / 10;
        for (int i = 0; i < 5; i++)
		{
            int n = 2 * i + 1;
            g.drawImage(Assets.ballHitpoint, (int)(dx * n - SIZE_BALL), (int)(HITBOX_CENTER - SIZE_BALL),SIZE_BALL*2,SIZE_BALL*2);
        }
        for(HelperLine h : helperLines)
        {
            h.Paint(g);
        }
        for(Connector tail:aliveTails)
        {
            tail.Paint(g);
        }

        for (List<Ball> bals : _balls)
		{
            for (Ball b : bals)
            {
                if (b.isShown()) {
                    paintBall(g, b);
                }
            }
        }
        //if (_explosionTicker > 0)
		//{
        //    if (_rand.nextDouble() > 0.05)
		//	{
        //        g.drawImage(Assets.explosion, 0, 680);
        //    }
		//	else
		//	{
        //  /      g.drawImage(Assets.explosionBright, 0, 680);
        //    }
        //    g.drawARGB((int) ((double) _explosionTicker / EXPLOSION_TIME * 255), 255, 255, 255);
        //}

        // Secondly, draw the UI above the game elements.
        if (state == GameState.Ready)
            drawReadyUI();
        if (state == GameState.Running)
            drawRunningUI();
        if (state == GameState.Paused)
            drawPausedUI();
        if (state == GameState.GameOver)
            drawGameOverUI();
    }

    //
    /*
        Draw bezier ribbon
        https://stackoverflow.com/a/30148733/8614565

    private void init(){
        paint = new Paint();

        paint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path = new Path();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        path.moveTo(34, 259);
        path.cubicTo(68, 151, 286, 350, 336, 252);
        canvas.drawPath(path, paint);

    }
     */
    Set<Connector> aliveTails = new ArraySet<>();
    Set<HelperLine> helperLines = new ArraySet<>();
    public static final int SIZE_BALL = 50;
    private void paintBall(Graphics g, Ball b)
	{
	    //b.type ==normal
        int sizeCoeff = (int)(SIZE_BALL*(1- (1-b.t)*(1-b.t)));
        Image imgToDraw = Assets.ballNormal;
        switch(b.flick)
        {
            case 0:
                if(b.mode == 0 || b.mode == 1){
                    imgToDraw = Assets.ballNormal;
                } else if(b.mode == 2)//slide
                {
                    imgToDraw = Assets.ballHitpoint;
                }
                break;
            case 1:
                imgToDraw = Assets.ballFlickLeft;
                break;
            case 2:
                imgToDraw = Assets.ballFlickRight;
                break;
        }

        if(b.helperLine !=null)
        {
            helperLines.add(b.helperLine);
        }
        Ball next = b.nextBall;
        if(next != null)
        {
            aliveTails.add(b.connector);
        }
        g.drawImage(imgToDraw, (int)(b.x - sizeCoeff), (int)(b.y - sizeCoeff),sizeCoeff*2,sizeCoeff*2);
/*        switch (b.type)
		{
            case Normal:
                g.drawImage(Assets.ballNormal, b.x - 90, b.y - 90);
                break;
            case OneUp:
                g.drawImage(Assets.ballOneUp, b.x - 90, b.y - 90);
                break;
            case Multiplier:
                g.drawImage(Assets.ballMultiplier, b.x - 90, b.y - 90);
                break;
            case Speeder:
                g.drawImage(Assets.ballSpeeder, b.x - 90, b.y - 90);
                break;
            case Bomb:
                g.drawImage(Assets.ballBomb, b.x - 90, b.y - 90);
                break;
            case Skull:
                g.drawImage(Assets.ballSkull, b.x - 90, b.y - 90);
                break;
            case FlickLeft:
                g.drawImage(Assets.ballFlickLeft, b.x - 90, b.y - 90);
                break;
            case FlickRight:
                g.drawImage(Assets.ballFlickRight, b.x - 90, b.y - 90);
                break;

        }
        */
    }

    private void nullify()
	{

        // Set all variables to null. You will be recreating them in the
        // constructor.
        _paintScore = null;

        // Call garbage collector to clean up memory.
        System.gc();
    }

    private void drawReadyUI()
	{
        Graphics g = game.getGraphics();

        g.drawARGB(155, 0, 0, 0);
        g.drawString("Tap to start!", game.getScreenX() / 2 - 270, game.getScreenY() / 2 - 250, _paintScore);

    }

    private void drawRunningUI()
	{
        Graphics g = game.getGraphics();

        if (_doubleMultiplierTicker > 0)
		{
            g.drawImage(Assets.sirens, 0, 100);
        }

        g.drawRect(0, 0, _gameWidth, 100, Color.BLACK);
        float ratioOfLife1 = (float) bundle.life / bundle.totalLife;
        float ratioOfLife2 = 0;
        if (ratioOfLife1 > 1)
		{
            ratioOfLife2 = ratioOfLife1 - 1;
            ratioOfLife1 = 1;
        }
        g.drawRect(0, 0, (int) (_gameWidth * 0.4f * ratioOfLife1), 50, ratioOfLife1 > 0.2f ? Color.GREEN : Color.RED);
        g.drawRect(0, 0, (int) (_gameWidth * 0.4f * ratioOfLife2), 50, Color.CYAN);
        //50-70-85-100
        float scoreRatio = (float) bundle.score / 10000.0f;
        g.drawRect((int) (_gameWidth * 0.5f), 0, (int) (_gameWidth * 0.5f * Math.min(scoreRatio, 1)), 50, GetScoreBkPaint(scoreRatio));
        //String s = "Score: " + _score +
        //        "   Multiplier: " + _multiplier * (_doubleMultiplierTicker > 0 ? 2 : 1) + "x" +
        //        "   Lifes remaining: " + _lives;
        DrawString(g, "" + bundle.score, (int) (_gameWidth * 0.55f), 80);
        //g.drawString(""+bundle.score, (int)(_gameWidth*0.52f), 80, _paintScore);
        g.drawString(bundle.combo + " COMBO", (int) (_gameWidth * 0.5f), 300, _paintScore);
        g.drawString(bundle.testResult.name(), (int) (_gameWidth * 0.5f), 350, _paintScore);
        String skills = deck.GetActivatedSkills();
        g.drawString(skills, (int) (_gameWidth * 0.5f), 400, _paintScore);

    }

    private void drawPausedUI()
	{
        Graphics g = game.getGraphics();
        g.drawARGB(155, 0, 0, 0);
        g.drawImage(Assets.pause, game.getScreenX() / 2 - 300, game.getScreenY() / 2 - 300);
        g.drawString("TAP TO CONTINUE", game.getScreenX() / 2, game.getScreenY() / 2, _paintGameover);
    }

    private void drawGameOverUI()
	{
        Graphics g = game.getGraphics();
        g.drawARGB(205, 0, 0, 0);
        g.drawImage(Assets.gameover, game.getScreenX() / 2 - 50, game.getScreenY() / 2 - 50);
        g.drawString("FINAL SCORE: " + bundle.score, game.getScreenX() / 2 - 50, game.getScreenY() / 2 - 25, _paintGameover);
    }

    private void DrawString(Graphics g, String s, int x, int y)
	{
        g.drawString(s, x, y, _paintScoreShadow);
        g.drawString(s, x, y, _paintScore);
    }

    private Paint GetScoreBkPaint(float ratio)
	{
        if (ratio < 0.5f)
            return _paintScoreD;
        if (ratio < 0.7f)
            return _paintScoreC;
        if (ratio < 0.85f)
            return _paintScoreB;
        if (ratio < 1)
            return _paintScoreA;
        return _paintScoreS;
    }

    @Override
    public void pause()
	{
        if (state == GameState.Running)
		{
            state = GameState.Paused;
            _currentTrack.pause();
        }

    }

    @Override
    public void resume()
	{
        if (state == GameState.Paused)
		{
            state = GameState.Running;
            _currentTrack.play();
        }
    }

    @Override
    public void dispose()
	{
        if (_currentTrack.isPlaying())
		{
            _currentTrack.stop();
        }
    }

    @Override
    public void backButton()
	{
        dispose();
    }
}
