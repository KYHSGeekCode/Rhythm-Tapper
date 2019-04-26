package sma.rhythmtapper.game.models;

import android.graphics.Path;

import sma.rhythmtapper.framework.*;
import sma.rhythmtapper.game.*;

import static sma.rhythmtapper.game.EnvVar.SIZE_BALL;

public class Tail extends Connector {
    private final boolean optimized;
    private boolean created = false;
    final int STROKE = 80;

    public Tail(Ball ball1, Ball ball2) {
        super(ball1, ball2);
        created = false;
        if (ball1.startLine == ball2.startLine && ball1.endLine == ball2.endLine) {
            optimized = true;
        } else {
            optimized = false;
        }
    }

    float[] tailxs;
    transient Path path;// = new Path();

    @Override
    public void Paint(Graphics g) {
        UpdateGhosts();
        if (path == null)
            path = new Path();
        if (optimized) {
            if (!created) {
                //	MakeTails();
            }
            //Draw optimized tail
            int prevx = 0, prevy = 0;
            prevx = ghost2x;
            prevy = ghost2y;
            //path.moveTo(ghost2x, ghost2y);
            int dy = ghost1y - ghost2y;
            for (int y = ghost2y + EnvVar.speed; y < ghost1y; y += EnvVar.speed) {
                float tt = (ball1.t - ball2.t) / (ghost1y - ghost2y) * (y - ghost1y) + ball1.t;
                int x = Ball.getXfromT(ball2.origx, ball1.endx, tt);
                g.drawLine(prevx, prevy, x, y, ball1.color, (int) (STROKE * GameScreen.getSizeCoeff(tt)));
                if (y > EnvVar.HITBOX_CENTER && prevy < EnvVar.HITBOX_CENTER) {
                    int helperx = (GameScreen.HITBOX_CENTER - y) * (prevx - x) / (prevy - y) + x;
                    g.drawImage(Assets.ballHitpoint, (int) (helperx - SIZE_BALL), (int) (EnvVar.HITBOX_CENTER - SIZE_BALL), SIZE_BALL * 2, SIZE_BALL * 2);
                }
                prevx = x;
                prevy = y;
            }
            g.drawLine(prevx, prevy, ghost1x, ghost1y, ball1.color, STROKE);
            if (ghost1y > EnvVar.HITBOX_CENTER && prevy < EnvVar.HITBOX_CENTER) {
                int helperx = (GameScreen.HITBOX_CENTER - ghost1y) * (prevx - ghost1x) / (prevy - ghost1y) + ghost1x;
                g.drawImage(Assets.ballHitpoint, (int) (helperx - SIZE_BALL), (int) (EnvVar.HITBOX_CENTER - SIZE_BALL), SIZE_BALL * 2, SIZE_BALL * 2);
            }

            //int starti = ghost2y
            //for(int i = starti; i < tailxs.length; i++) {
            //	y += EnvVar.speed;
            //	path.lineTo(tailxs[i], y);
            //}
            //path.close();
            //g.DrawPath(path);
        } else {
            //Create Dynamic tail
            //
            //path.reset();
            int prevx = 0, prevy = 0;
            prevx = ghost2x;
            prevy = ghost2y;
            int dy = ghost1y - ghost2y;
            for (int y = ghost2y + EnvVar.speed; y < ghost1y; y += EnvVar.speed) {
                float tt = (float) (y - ghost2y) / (float) (dy);
                float sizett = (ball1.t - ball2.t) / (ghost1y - ghost2y) * (y - ghost1y) + ball1.t;
                int x = Ball.getXfromT(ghost2x, ghost1x, tt);
                g.drawLine(prevx, prevy, x, y, ball1.color, (int) (STROKE * GameScreen.getSizeCoeff(sizett)));
                if (y > EnvVar.HITBOX_CENTER && prevy < EnvVar.HITBOX_CENTER) {
                    int helperx = (GameScreen.HITBOX_CENTER - y) * (prevx - x) / (prevy - y) + x;
                    g.drawImage(Assets.ballHitpoint, (int) (helperx - SIZE_BALL), (int) (EnvVar.HITBOX_CENTER - SIZE_BALL), SIZE_BALL * 2, SIZE_BALL * 2);
                }
                prevx = x;
                prevy = y;
                //path.lineTo(x,y);
            }
            g.drawLine(prevx, prevy, ghost1x, ghost1y, ball1.color, STROKE);
            if (ghost1y > EnvVar.HITBOX_CENTER && prevy < EnvVar.HITBOX_CENTER) {
                int helperx = (GameScreen.HITBOX_CENTER - ghost1y) * (prevx - ghost1x) / (prevy - ghost1y) + ghost1x;
                g.drawImage(Assets.ballHitpoint, (int) (helperx - SIZE_BALL), (int) (EnvVar.HITBOX_CENTER - SIZE_BALL), SIZE_BALL * 2, SIZE_BALL * 2);
            }

            //path.lineTo(ghost1x,ghost1y);
            //path.close();
            //g.DrawPath(path);
        }
        int x = ghost2x;
        int y = ghost2y;
        //for(int i=0;i<tailxs.length;i++)
        {
            //x = tailxs[i];
            //y =
        }
        //g.drawLine(ghost1x, ghost1y, ghost2x, ghost2y,ball1.color,40);

        return;
    }

    public void MakeTails() {
        final int delta = EnvVar.speed;
        int dy = ghost1y - ghost2y;
        int slices = dy / delta;
        final float dt = dy / slices;
        if (slices <= 1)
            return;
        tailxs = new float[slices - 1];
        float tt = 0;
        for (int i = 0; i < slices - 1; i++) {
            tailxs[i] = Ball.getXfromT(ghost2x, ghost1x, tt);
            tt += dt;
        }
    }
}
