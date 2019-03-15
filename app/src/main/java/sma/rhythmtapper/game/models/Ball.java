package sma.rhythmtapper.game.models;
import java.util.*;
import java.io.*;

//This class may become a mammoth class!!
public class Ball implements Serializable{
    public enum BallType {
        Normal, OneUp, Multiplier, Speeder, Bomb, Skull,
		LongDown,LongUp,FlickLeft,FlickRight,Slide;
		public BallType valueOf()
		{
			return null;
		}
    }
	public boolean startOfFlick;
    public int x;
    public int y;
	int origx;
    public BallType type;
    private double speedMultiplier;
	static Random random=new Random();
    public Ball(int x, int y, BallType type){
        this.x = x;
        this.y = y;
		this.origx=x;
        this.type = type;
        this.speedMultiplier = type == BallType.Speeder ? 1.4 : 1;
    }

    public void update(int speed) {
		//todo add fake move
		//: Use bezier 
        this.y += speed * speedMultiplier;
		//this.x = origx+(int)(50*Math.sin(y));
    }
}
