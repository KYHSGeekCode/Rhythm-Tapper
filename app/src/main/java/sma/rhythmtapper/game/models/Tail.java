package sma.rhythmtapper.game.models;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import sma.rhythmtapper.framework.Graphics;
import sma.rhythmtapper.game.EnvVar;

import static sma.rhythmtapper.game.models.Ball.DEPTH;
import static sma.rhythmtapper.game.models.Ball.alphaDepth;

//ㅇㅁㅇㅁㅇㅁㅇㅁㅇ
public class Tail extends Connector {
    private final boolean optimized;
    private boolean created = false;
    final int STROKE = 100;

    final int TAILCOLOR = Color.argb(0x99, 0xFF, 0xFF, 0xFF);

    public Tail(Ball ball1, Ball ball2) {
        super(ball1, ball2);
        created = false;
        optimized = ball1.startLine == ball2.startLine && ball1.endLine == ball2.endLine;
    }

    float[] tailxs;
//    transient Path path;// = new Path();

    final int numJoints = 6;

//    @Override
//    public void Paint(Graphics g) {
//        //super.Paint(g);
//        //각 관절마다 sizecoeff를 체크하여 그것을 넘지 못하면 그리지 않는다.
//        //ball1과 ball2 사이를 보간하여 가상의 공들을 만들고 그 사이를 직선으로 잇는다.
//        //ball1과 ball2 사이의 z들을 잘라서 그 z에 대해 좌표들을 계산하면 될 듯.
//        int prevX, prevY;
//        prevX = ball1.x;
//        prevY = ball1.y;
//        for (int k = 1; k < numJoints - 2; k++) {
//            float alp = (float) k / (float) numJoints;
//            float z = alp * ball1.z + (1 - alp) * ball2.z;
//            int y;// = (int)(ball1.aOfZ*(z-Ball.alphaDepth)*(z-Ball.alphaDepth));
//            //this.y = (int)(/*GameScreen.BALL_INITIAL_Y*/ EnvVar.HITBOX_CENTER+ ( EnvVar.currentTime-this.time)* EnvVar.speed *100);
//            float t = 1 - ((float) z / DEPTH);//(float)EnvVar.HITBOX_CENTER;
//            int x = (int) (alp * ball1.x + (1 - alp) * ball2.x);//Ball.getXfromT(ball2.x,0,t);
//            y = (int) (alp * ball1.y + (1 - alp) * ball2.y);
//            if (t < 0.3f || t > 1.0f)
//                continue;
//            g.drawLine(prevX, prevY, x, y, Color.WHITE, STROKE);
//            prevX = x;
//            prevY = y;
//        }
//        g.drawLine(prevX, prevY, ball2.x, ball2.y, Color.WHITE, STROKE);
//
//
////        UpdateGhosts();
////
////       if (path == null)
////            path = new Path();
//        /*
//        if (optimized) {
//            if (!created) {
//                //	MakeTails();
//            }
//            //Draw optimized tail
//            int prevx = 0, prevy = 0, prevz = 0;
//            prevx = ghost2x;
//            prevy = ghost2y;
//            prevz = ball2.z;
//
//            //path.moveTo(ghost2x, ghost2y);
//            int dy = ghost1y - ghost2y;
//            for (int y = ball2.z + EnvVar.speed; z < ball1.z; z += EnvVar.speed) {
//                float tt = (ball1.t - ball2.t) / (ball1.z-ball2.z/*ghost1y - ghost2y* /) * (y - ghost1y) + ball1.t;
//                int x = Ball.getXfromT(ball2.origx, ball1.endx, tt);
//                g.drawLine(prevx, prevy, x, y, ball1.color, (int) (STROKE * GameScreen.getSizeCoeff(tt)));
//                if (y > EnvVar.HITBOX_CENTER && prevy < EnvVar.HITBOX_CENTER) {
//                    int helperx = (GameScreen.HITBOX_CENTER - y) * (prevx - x) / (prevy - y) + x;
//                    g.drawImage(Assets.ballHitpoint, (int) (helperx - SIZE_BALL), (int) (EnvVar.HITBOX_CENTER - SIZE_BALL), SIZE_BALL * 2, SIZE_BALL * 2);
//                }
//                prevx = x;
//                prevy = y;
//            }
//            g.drawLine(prevx, prevy, ghost1x, ghost1y, ball1.color, STROKE);
//            if (ghost1y > EnvVar.HITBOX_CENTER && prevy < EnvVar.HITBOX_CENTER) {
//                int helperx = (GameScreen.HITBOX_CENTER - ghost1y) * (prevx - ghost1x) / (prevy - ghost1y) + ghost1x;
//                g.drawImage(Assets.ballHitpoint, (int) (helperx - SIZE_BALL), (int) (EnvVar.HITBOX_CENTER - SIZE_BALL), SIZE_BALL * 2, SIZE_BALL * 2);
//            }
//
//            //int starti = ghost2y
//            //for(int i = starti; i < tailxs.length; i++) {
//            //	y += EnvVar.speed;
//            //	path.lineTo(tailxs[i], y);
//            //}
//            //path.close();
//            //g.DrawPath(path);
//        } else {
//            //Create Dynamic tail
//            //
//            //path.reset();
//            int prevx = 0, prevy = 0;
//            prevx = ghost2x;
//            prevy = ghost2y;
//            int dy = ghost1y - ghost2y;
//            for (int y = ghost2y + EnvVar.speed; y < ghost1y; y += EnvVar.speed) {
//                float tt = (float) (y - ghost2y) / (float) (dy);
//                float sizett = (ball1.t - ball2.t) / (ghost1y - ghost2y) * (y - ghost1y) + ball1.t;
//                int x = Ball.getXfromTLinear(ghost2x, ghost1x, tt);
//                g.drawLine(prevx, prevy, x, y, ball1.color, (int) (STROKE * GameScreen.getSizeCoeff(sizett)));
//                if (y > EnvVar.HITBOX_CENTER && prevy < EnvVar.HITBOX_CENTER) {
//                    int helperx = (GameScreen.HITBOX_CENTER - y) * (prevx - x) / (prevy - y) + x;
//                    g.drawImage(Assets.ballHitpoint, (int) (helperx - SIZE_BALL), (int) (EnvVar.HITBOX_CENTER - SIZE_BALL), SIZE_BALL * 2, SIZE_BALL * 2);
//                }
//                prevx = x;
//                prevy = y;
//                //path.lineTo(x,y);
//            }
//            g.drawLine(prevx, prevy, ghost1x, ghost1y, ball1.color, STROKE);
//            if (ghost1y > EnvVar.HITBOX_CENTER && prevy < EnvVar.HITBOX_CENTER) {
//                int helperx = (GameScreen.HITBOX_CENTER - ghost1y) * (prevx - ghost1x) / (prevy - ghost1y) + ghost1x;
//                g.drawImage(Assets.ballHitpoint, (int) (helperx - SIZE_BALL), (int) (EnvVar.HITBOX_CENTER - SIZE_BALL), SIZE_BALL * 2, SIZE_BALL * 2);
//            }
//
//            //path.lineTo(ghost1x,ghost1y);
//            //path.close();
//            //g.DrawPath(path);
//        }
//        int x = ghost2x;
//        int y = ghost2y;
//        //for(int i=0;i<tailxs.length;i++)
//        {
//            //x = tailxs[i];
//            //y =
//        }
//        //g.drawLine(ghost1x, ghost1y, ghost2x, ghost2y,ball1.color,40);
//
//        return;
//
//         */
//    }

    @Override
    public void Paint(Graphics g) {
        float m1 = ball1.origx;
        float n1 = ball1.endx;
        float m2 = ball2.origx;
        float n2 = ball2.endx;
        float timeDelta = ball2.time - ball1.time;
        final float dt = 0.1f;
        float prevX = ball1.x;
        float prevY = ball1.y;
        float prevprevX = ball1.x;
        float prevprevY = ball1.y;
        Paint paint = new Paint();
        paint.setStrokeWidth(STROKE);
        paint.setColor(TAILCOLOR);
        paint.setStyle(Paint.Style.FILL);
        // m1...n1
        // ...
        // m2...n2
        // 사이를 일정 시간 간격으로 보간
        // 가상의 ball 들을 만든다.
        // 현재에 맞는 t들을 생성해야 한다.
        // 먼저 이 가상 점들이 생성될 시간을 계산해야 한다.
        float aOfZ = EnvVar.HITBOX_CENTER / (alphaDepth * alphaDepth);
        float time;
        final float initial_stroke_half = STROKE / 2.0f;
        Path path = new Path();
        path.moveTo(prevX - initial_stroke_half, prevY);
        path.lineTo(prevX + initial_stroke_half, prevY);
        int count = 0;
        for (time = ball1.time; time <= ball2.time; time += dt)
            count++;
        float[] xs = new float[count];
        float[] ys = new float[count];
        int i = 0;
        for (time = ball1.time; time <= ball2.time; time += dt) {
            // time : 가상 점들이 도착하는 시간
            // m, n: 가상 점들의 시작점과 끝점
            // t: 현재 시간에서 도착까지 남은 시간 (초)의 음수
            float t = EnvVar.currentTime - time + 1;
            if (t <= 0)
                continue;
            // m = lerp(m1, m2, factor);
            // n = lerp(n1, n2, factor);
            // x = lerp(m, n, t);

            float factor = (time - ball1.time) / timeDelta;
            float m = m2 * factor + m1 * (1 - factor); // m1, m2를 배분
            float n = n2 * factor + n1 * (1 - factor); // m1, m2를 배분
            float x = mn2x(m, n, t);
            float z = (int) (DEPTH * (time - EnvVar.currentTime) * EnvVar.speed / 50);
            float y = (int) (aOfZ * (z - alphaDepth) * (z - alphaDepth));

            float stroke_half = initial_stroke_half * t;
            path.lineTo(x + stroke_half, y);
            xs[i] = x - stroke_half;
            ys[i] = y;
            i++;
//            g.drawLine((int) prevprevX, (int) prevprevY, (int) x, (int) y, TAILCOLOR, STROKE);

            prevprevX = prevX;
            prevprevY = prevY;
            prevX = x;
            prevY = y;

        }
//        if(time < ball2.time)
        if (ball2.y <= prevY) {
//            g.drawLine((int) prevprevX, (int) prevprevY, ball2.x, ball2.y, TAILCOLOR, STROKE);
            float t = EnvVar.currentTime - ball2.time + 1;
            float stroke_half = initial_stroke_half * t;
            path.lineTo(ball2.x + stroke_half, ball2.y);
            path.lineTo(ball2.x - stroke_half, ball2.y);
        }
        for (int k = count - 1; k >= 0; k--) {
            path.lineTo(xs[k], ys[k]);
        }
        path.close();
        g.DrawPath(path, paint);
    }

    public float mn2x(float m, float n, float t) {
        return n * t + m * (1.0f - t);
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
