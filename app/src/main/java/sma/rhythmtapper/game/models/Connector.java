package sma.rhythmtapper.game.models;

import android.graphics.Color;

import java.io.Serializable;

import sma.rhythmtapper.framework.Graphics;
import sma.rhythmtapper.game.GameScreen;

public class Connector implements Serializable {
    private String TAG = "Connector";
    public Ball ball1, ball2;
    //ghost 1 is lower
    int ghost1x, ghost1y;
    int ghost2x, ghost2y;

    float difftime;
    float diffY;

    final int COLORCONNECTOR = Color.argb(0x77, 0xFF, 0xFF, 0xFF);


    //Ball1 is first (lower)
    public Connector(Ball ball1, Ball ball2) {
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
        if (ball1.alive)
            g.drawLine(ghost1x, ghost1y, ghost2x, ghost2y, COLORCONNECTOR, (int) (50 * GameScreen.getSizeCoeff(ball1.t)));
        /*if(ghost1y > EnvVar.HITBOX_CENTER && ghost2y <EnvVar.HITBOX_CENTER) {
            int helperx = (GameScreen.HITBOX_CENTER - ghost1y) * (ghost2x - ghost1x) / (ghost2y - ghost1y) + ghost1x;
            g.drawImage(Assets.ballHitpoint, (int) (helperx - SIZE_BALL), (int) (EnvVar.HITBOX_CENTER - SIZE_BALL)
			, SIZE_BALL * 2, SIZE_BALL * 2);
        }*/
        return;
    }

    protected void UpdateGhosts() {
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
