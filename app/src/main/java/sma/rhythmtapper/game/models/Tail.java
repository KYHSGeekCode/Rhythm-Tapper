package sma.rhythmtapper.game.models;

import android.graphics.Color;
import android.graphics.Path;

import sma.rhythmtapper.framework.Graphics;
import sma.rhythmtapper.game.EnvVar;

import static sma.rhythmtapper.game.models.Ball.DEPTH;

//ㅇㅁㅇㅁㅇㅁㅇㅁㅇ
public class Tail extends Connector {
    private final boolean optimized;
    private boolean created = false;
    final int STROKE = 80;

    public Tail(Ball ball1, Ball ball2) {
        super(ball1, ball2);
        created = false;
        optimized = ball1.startLine == ball2.startLine && ball1.endLine == ball2.endLine;
    }

    float[] tailxs;
    transient Path path;// = new Path();

    final int numJoints = 6;

    @Override
    public void Paint(Graphics g) {
        //super.Paint(g);
        //각 관절마다 sizecoeff를 체크하여 그것을 넘지 못하면 그리지 않는다.
        //ball1과 ball2 사이를 보간하여 가상의 공들을 만들고 그 사이를 직선으로 잇는다.
        //ball1과 ball2 사이의 z들을 잘라서 그 z에 대해 좌표들을 계산하면 될 듯.
        int prevX, prevY;
        prevX = ball1.x;
        prevY = ball1.y;
        for (int k = 1; k < numJoints - 2; k++) {
            float alp = (float) k / (float) numJoints;
            float z = alp * ball1.z + (1 - alp) * ball2.z;
            int y;// = (int)(ball1.aOfZ*(z-Ball.alphaDepth)*(z-Ball.alphaDepth));
            //this.y = (int)(/*GameScreen.BALL_INITIAL_Y*/ EnvVar.HITBOX_CENTER+ ( EnvVar.currentTime-this.time)* EnvVar.speed *100);
            float t = 1 - ((float) z / DEPTH);//(float)EnvVar.HITBOX_CENTER;
            int x = (int) (alp * ball1.x + (1 - alp) * ball2.x);//Ball.getXfromT(ball2.x,0,t);
            y = (int) (alp * ball1.y + (1 - alp) * ball2.y);
            if (t < 0.3f || t > 1.0f)
                continue;
            g.drawLine(prevX, prevY, x, y, Color.WHITE, 30);
            prevX = x;
            prevY = y;
        }
        g.drawLine(prevX, prevY, ball2.x, ball2.y, Color.WHITE, 30);


//        UpdateGhosts();
//
//       if (path == null)
//            path = new Path();
        /*
        if (optimized) {
            if (!created) {
                //	MakeTails();
            }
            //Draw optimized tail
            int prevx = 0, prevy = 0, prevz = 0;
            prevx = ghost2x;
            prevy = ghost2y;
            prevz = ball2.z;

            //path.moveTo(ghost2x, ghost2y);
            int dy = ghost1y - ghost2y;
            for (int y = ball2.z + EnvVar.speed; z < ball1.z; z += EnvVar.speed) {
                float tt = (ball1.t - ball2.t) / (ball1.z-ball2.z/*ghost1y - ghost2y* /) * (y - ghost1y) + ball1.t;
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
                int x = Ball.getXfromTLinear(ghost2x, ghost1x, tt);
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

         */
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

    public void Draw(Graphics g) {
        int x, y, z;
        int prevx, prevy, prevz;
        int startx, starty, startz;
        int endx, endy, endz;
        startx = ball1.x;
        starty = ball1.y;
        startz = ball1.z;
        endx = ball2.x;
        endy = ball2.y;
        endz = ball2.z;
        int deltaz = EnvVar.speed;
        prevx = startx;
        prevy = starty;
        prevz = startz;
        for (z = startz + deltaz; z < endz; z += deltaz) {
            //    x = solvex(z); //직선
            //    y = solvey(z); // 포물선
            //    g.drawLine(x,y,prevx,prevy,ball1.color,STROKE);
            //    prevx = x;
            //    prevy = y;
            //    prevz  =z;
        }
        g.drawLine(prevx, prevy, endx, endy, ball1.color, STROKE);
    }
    //고스트 알고리즘
    //롱이나 슬라이드가 처음 판정선에 도착했을 때, 그 위치에 고스트 생성
    // 그 고스트는 다음 관절의 위치에 따라 적당한 위치(x절편이라던가 아니면 원래 그 위치)에 존재하자
    // 그 고스트를 이용해서 다른 관절들을 움지기인다.
    //애초에 관절들이 제대로 움직이면 되는 거다.,
}
