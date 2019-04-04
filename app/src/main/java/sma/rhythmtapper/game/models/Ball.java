package sma.rhythmtapper.game.models;
import java.util.*;
import java.io.*;

//This class may become a mammoth class!!
public class Ball implements Serializable
{

	public Ball(int id, int color, int mode, int flick, float time, float startLine, float endLine, int[] previds)
	{
		this.id = id;
		this.color = color;
		this.mode = mode;
		this.flick = flick;
		this.time = time;
		this.startLine = startLine;
		this.endLine = endLine;
		this.previds = previds;
	}
    public enum BallType {
        Normal, OneUp, Multiplier, Speeder, Bomb, Skull,
		LongDown,LongUp,FlickLeft,FlickRight,Slide;
		public BallType valueOf()
		{
			return null;
		}
    }
    public int startLane, endLane;
    public int showFrame;
    public int missFrame;
    public int thread;

	public boolean startOfFlick;
    public int x;
    public int y;
	int origx;
    public BallType type;
    //private double speedMultiplier;
	static Random random=new Random();
	
	public int id;
	public int color;
	public int mode;
	public int flick;
	public float time;
	public float startLine;
	public float endLine;
	public int [] previds;
	
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

    public void update(int speed) {
		//todo add fake move
		//: Use bezier 
        this.y += speed; // * speedMultiplier;
		//this.x = origx+(int)(50*Math.sin(y));
    }
}
