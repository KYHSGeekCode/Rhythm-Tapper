package sma.rhythmtapper.game.models;

import android.util.Log;

import sma.rhythmtapper.framework.Impl.*;
import sma.rhythmtapper.game.*;

import java.io.*;

import sma.rhythmtapper.framework.*;

public class Tail implements Serializable {
    private String TAG = "Tail";
    public Ball ball1, ball2;
    //ghost 1 is lower
    int ghost1x, ghost1y;
    int ghost2x, ghost2y;

    float difftime;
    float diffY;

    //Ball1 is first (lower)
    public Tail(Ball ball1, Ball ball2) {
        this.ball1 = ball1;
        this.ball2 = ball2;
        //this.difftime = ball2.time - ball1.time;
    }

    public void Paint(Graphics g) {
//		if (ball1.alive && ball2.alive)
//		{
//
//		}
//		else
        //	{
        UpdateGhosts();
        //	}

        g.drawLine(ghost1x, ghost1y, ghost2x, ghost2y,ball1.color,40);
        return;
    }

    private void UpdateGhosts() {
        //if(ball1.alive==false)
        //{
        //diffY = difftime*EnvVar.speed;
        //ghost1y = (int)((EnvVar.currentTime - ball1.time) * EnvVar.speed * 100 /*+ GameScreen.BALL_INITIAL_Y*/);
        ///float t1 = ((float)ghost1y) / EnvVar.HITBOX_CENTER;
        //Log.d(TAG,"t1="+t1);
        //ghost1x = ball1.getXfromT(t1);
        //ghost2y = (int)((EnvVar.currentTime - ball2.time) * EnvVar.speed * 100 /*+ GameScreen.BALL_INITIAL_Y*/);
        //float t2 = ((float)ghost2y) / EnvVar.HITBOX_CENTER;
        //Log.d(TAG,"t2="+t2);
        //ghost2x = ball2.getXfromT(t2);
        //if(ball1.isAlive()) {
        ghost1x = ball1.x;
        ghost1y = ball1.y;
        //}
        //if(ball2.isAlive())
        //{
        ghost2x = ball2.x;
        ghost2y = ball2.y;
        //}

        //}
        return;
    }
}
