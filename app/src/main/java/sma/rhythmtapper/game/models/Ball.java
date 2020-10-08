package sma.rhythmtapper.game.models;

import java.io.Serializable;
import java.util.Random;

import sma.rhythmtapper.framework.Graphics;
import sma.rhythmtapper.game.EnvVar;
import sma.rhythmtapper.game.HelperLine;

//This class may become a mammoth class!!
public class Ball implements Serializable {
    public static final int DEPTH = 1000;

    private static final String TAG = "Ball";
    public float aOfZ;

    public Ball(int id, int color, int mode, int flick, float time, float startLine, float endLine, int[] previds) {
        this.id = id;
        this.color = color;
        this.mode = mode;
        this.flick = flick;
        this.time = time;
        this.startLine = startLine;
        this.endLine = endLine;
        this.previds = previds;
        alive = false;
    }

    private void RemoveSelf() {

    }

    void Erase() {
        if (connector == null)
            return;
        connector.ball2.Erase();
        RemoveSelf();
    }

    public boolean isSlideMiddle() {
        return isSlideNote() && connector != null;// && FIXME
    }

    public boolean isSlideEnd() {
        return isSlideNote() && connector == null;
    }

    public boolean isSlideDownOrMiddle() {
        return connector != null && isSlideNote();
    }

    //FixMe
    public boolean isSlideStart() {
        return isSlideNote() && connector != null;
    }

    public boolean isLongUp() {
        return isLongNote() && connector == null;
    }

    public boolean isNormal() {
        return (!isFlick()) && (mode == 0);
    }

    public boolean isFlick() {
        return flick > 0;
    }

    public boolean isLongDown() {
        return isLongNote() && connector != null;
    }

    public boolean isLongNote() {
        return mode == 1;
    }

    public boolean isSlideNote() {
        return mode == 2;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isShown() {
        return y >= -50 && y <= EnvVar.gameHeight + 50;
    }

    public boolean shouldDie() {
        return z < -20;
    }


    public enum BallType {
        Normal, OneUp, Multiplier, Speeder, Bomb, Skull,
        LongDown, LongUp, FlickLeft, FlickRight, Slide;

        public BallType valueOf() {
            return null;
        }
    }

    //public int startLane, endLane;
    public int showFrame;
    public int missFrame;
    public int thread;

    public boolean startOfFlick;
    public int x;
    public int y;
    public int z;
    public BallType type;
    //private double speedMultiplier;
    static Random random = new Random();

    int origx;
    int endx;
//	int origy;

    public int id;
    public int color;
    public int mode;
    public int flick;
    public float time;
    public float startLine;
    public float endLine;
    public int[] previds;
    //public int nextid;
    public Ball nextBall;
    public float t = 0;

    public boolean alive;

    public Connector connector;
    public HelperLine helperLine;

    public void OnGameStart() {
        //Log.v(TAG,"Width="+EnvVar.gameWidth);
        OnScreenMeasured();
        //this.y = (int)(GameScreen.BALL_INITIAL_Y + ( EnvVar.currentTime-this.time)* speed *100);
        alive = true;
        //Log.d("Ball","endline"+endLine+"startLine"+startLine+"origx"+origx+"endx"+endx);
    }

    public void update(int speed) {
        // Bezier : p(t) = (1-t)^2A +t^2C
        // t = 0 : A x = startlane
        // t = 1: B x = endlane
        //this.y += speed;
        this.z = (int) (DEPTH * (this.time - EnvVar.currentTime) * EnvVar.speed / 50);
        this.y = (int) (aOfZ * (z - alphaDepth) * (z - alphaDepth));
        //this.y = (int)(/*GameScreen.BALL_INITIAL_Y*/ EnvVar.HITBOX_CENTER+ ( EnvVar.currentTime-this.time)* EnvVar.speed *100);
        t = 1 - ((float) this.z / DEPTH);//(float)EnvVar.HITBOX_CENTER;
        this.x = getXfromT(t);

        //Log.v("Ballx","("+x+","+y+","+z+"),"+"alphaDEPTH 800 aOfZ 0.00154375alphaOfZ0.8");
        //: Use bezier
        // * speedMultiplier;
        //this.x = origx+(int)(50*Math.sin(y));
    }

    public void Paint(Graphics g) {

    }

    public int getXfromT(float tt) {
        if (this.origx == 0) {
            OnScreenMeasured();
        }
        return getXfromT(origx, endx, tt);
    }

    static final float alphaOfZ = 0.7f;//0.95f;
    static final float alphaDepth = alphaOfZ * DEPTH;

    private void OnScreenMeasured() {
        this.origx = (int) ((EnvVar.gameWidth * 0.8f / 5 / 2) * (2 * startLine - 1) + EnvVar.gameWidth * 0.1f);
        this.endx = (int) ((EnvVar.gameWidth / 5 / 2) * (2 * endLine - 1));
        this.aOfZ = EnvVar.HITBOX_CENTER / (alphaDepth * alphaDepth);
    }

    public static int getXfromT(int start, int end, float tt) {
        return getXfromTLinear(start, end, tt);
/*		if(tt>=1f)
			return end;
		int a = start-end;
		return (int)(a*(tt-1)*(tt-1)+end);
		*/
    }

    public static int getXfromTLinear(int start, int end, float tt) {
        if (tt >= 1f)
            return end;
        int a = start - end;
        return (int) (a * (1 - tt) + end);
    }


	/*
    public Ball(int x, int y, BallType type){
        this.x = x;
        this.y = y;
		this.origx=x;
        this.type = type;
        //this.speedMultiplier = type == BallType.Speeder ? 1.4 : 1;
    }

	//public ball
    public Ball (int startLane, int endLane, BallType bt, int startFrame, int thread)
    {
        this.startLane=startLane;
        this.endLane=endLane;
        this.type=bt;
        this.showFrame=startFrame;
        this.missFrame=showFrame+10;
        this.thread=thread;
    }
*/
}
