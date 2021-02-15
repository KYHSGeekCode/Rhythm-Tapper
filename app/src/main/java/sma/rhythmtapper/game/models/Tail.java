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
    final int STROKE = 1;

    final int TAILCOLOR = Color.argb(0x99, 0xFF, 0xFF, 0xFF);

    public Tail(Ball ball1, Ball ball2) {
        super(ball1, ball2);
        created = false;
        optimized = ball1.startLine == ball2.startLine && ball1.endLine == ball2.endLine;
    }

    float[] tailxs;
//    transient Path path;// = new Path();

    final int numJoints = 6;

    @Override
    public void Paint(Graphics g) {
        float m1 = ball1.origx;
        float n1 = ball1.endx;
        float m2 = ball2.origx;
        float n2 = ball2.endx;
        final float timeDelta = ball2.time - ball1.time;
        final float dt = 0.1f;
        float prevX = ball1.x;
        float prevY = ball1.y;
        Paint paint = new Paint();
        paint.setStrokeWidth(5);
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
        final float initial_stroke_half = STROKE / 2.0f;
        Path path = new Path();
        float firstt = EnvVar.currentTime - ball2.time + 1;

        path.moveTo(prevX - initial_stroke_half, prevY);
        path.lineTo(prevX + initial_stroke_half, prevY);

        // count number of points to interpolate
        float timeLength = timeDelta * EnvVar.speed;
        int count = (int) Math.ceil(timeLength / 2) + 1;

        float[] xs = new float[count];
        float[] ys = new float[count];
        float[] dxs = new float[count - 1];
        float[] dys = new float[count - 1];
        float[] rightXs = new float[count];
        float[] rightYs = new float[count];

        for (int i = 0; i < count; i++) {
            // time : 가상 점들이 도착하는 시간
            // m, n: 가상 점들의 시작점과 끝점
            // t: 현재 시간에서 도착까지 남은 시간 (초)의 음수
            float time = ball1.time + (timeDelta * i) / count;
            float t = EnvVar.currentTime - time + 1;
//            if (t < 0 || t > 1)
//                continue;
            // m = lerp(m1, m2, factor);
            // n = lerp(n1, n2, factor);
            // x = lerp(m, n, t);

            float factor = (time - ball1.time) / timeDelta;
            float m = m2 * factor + m1 * (1 - factor); // m1, m2를 배분
            float n = n2 * factor + n1 * (1 - factor); // m1, m2를 배분
            float x = mn2x(m, n, t);
            float z = (int) (DEPTH * (time - EnvVar.currentTime) * EnvVar.speed / 50);
            float y = (int) (aOfZ * (z - alphaDepth) * (z - alphaDepth));

            xs[i] = x;
            ys[i] = y;
        }

        // 기울기들 계산
        for (int i = 0; i < count - 1; i++) {
            dxs[i] = xs[i + 1] - xs[i];
            dys[i] = ys[i + 1] - ys[i];
        }

        // 모서리 좌표 계산
        for (int i = 0; i < count - 1; i++) {
            float rightX1 = xs[i] - dys[i];
            float rightX2 = xs[i] - dys[i + 1];
            float rightY1 = ys[i] + dxs[i];
            float rightY2 = ys[i] + dxs[i + 1];
            float rightX = (rightX1 + rightX2) / 2;
            float rightY = (rightY1 + rightY2) / 2;
            rightXs[i] = rightX;
            rightYs[i] = rightY;
        }
        rightXs[count - 1] = xs[count - 1] - dys[count - 1];

//        if(time < ball2.time)
        if (ball2.y <= prevY) {
//            g.drawLine((int) prevprevX, (int) prevprevY, ball2.x, ball2.y, TAILCOLOR, STROKE);
            float t = EnvVar.currentTime - ball2.time + 1;
            float dx = ball2.x - prevX;
            float dy = ball2.y - prevY;

            dx *= STROKE * t;
            dy *= STROKE * t;

//            float stroke_half = initial_stroke_half * t;
            path.lineTo(ball2.x - dy, ball2.y - dx);
            path.lineTo(ball2.x + dy, ball2.y + dx);
        }
        for (
                int k = count - 1;
                k >= 0; k--) {
            path.lineTo(xs[k] + dys[k], ys[k] + dxs[k]);
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
