package sma.rhythmtapper.framework.Impl;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import sma.rhythmtapper.framework.*;
import sma.rhythmtapper.game.*;
import sma.rhythmtapper.game.NoteFile.*;
import sma.rhythmtapper.game.models.*;
import wseemann.media.FFmpegMediaMetadataRetriever;


public class RTGame extends Activity implements Game {
    RTFastRenderView renderView;
    Graphics graphics;
    Audio audio;
    Audio fileAudio;
    Input input;
    FileIO fileIO;
    Screen screen;
    PowerManager.WakeLock wakeLock;

    Point screensize=new Point();

    public ChooseSongScreen chooseSongScreen;

	int ballspeed;
	NoteFile noteFile;
	Deck deck;

	public void setAutoPlay(boolean au)
	{
		EnvVar.autoPlay = au;
	}
	public void setBallspeed(int ballspeed)
	{
		this.ballspeed = ballspeed;
	}

	public int getBallspeed()
	{
		return ballspeed;
	}

	public void setNoteFile(NoteFile noteFile)
	{
		this.noteFile = noteFile;
	}

	public NoteFile getNoteFile()
	{
		return noteFile;
	}

	public void setDeck(Deck deck)
	{
		this.deck = deck;
	}

	public Deck getDeck()
	{
		return deck;
	}
	
	//difficulty notefild deck speed
	
	
    @Override
    public int getScreenX()
    {
        return  screensize.x;
    }

    @Override
    public int getScreenY()
    {
        return  screensize.y;
    }

    @Override
    public void goToActivity(Class<?> activity) {
        Intent i = new Intent(this, activity);
        // add flag, when activity already runs,
        // use it instead of launching a new instance
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public FFmpegMediaMetadataRetriever createVideo(String video) {
	    if(video == null)
	        return null;
	    FFmpegMediaMetadataRetriever med = new FFmpegMediaMetadataRetriever();
	    med.setDataSource(video);
        return med;
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        //Point size=new Point();
        getWindowManager().getDefaultDisplay().getSize(screensize);
        int frameBufferWidth = screensize.x;//1080;
        int frameBufferHeight = screensize.y;//1920;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight, Bitmap.Config.RGB_565);

        float scaleX = (float) frameBufferWidth
                / screensize.x;
        float scaleY = (float) frameBufferHeight
                / screensize.y;

        renderView = new RTFastRenderView(this, frameBuffer);
        graphics = new RTGraphics(getAssets(), frameBuffer);
        fileIO = new RTFileIO(this);
        audio = new RTAudio(this);
        fileAudio = new RTFileAudio(this);
        input = new RTInput(this, renderView, scaleX, scaleY);
        screen = getInitScreen();
        setContentView(renderView);

        //TODO: use window flag instead
        //https://developer.android.com/training/scheduling/wakelock
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "RhythmTapper::wakelock");

        chooseSongScreen=new ChooseSongScreen(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        wakeLock.acquire();
        screen.resume();
        renderView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        wakeLock.release();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public Vibrator getVibrator() {
        return (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public Audio getFileAudio() {
        return fileAudio;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    @Override
    public Screen getCurrentScreen() {
        return screen;
    }

    @Override
    public Screen getInitScreen() {
        return null;
    }
}
