package sma.rhythmtapper.framework.Impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class RTFastRenderView extends SurfaceView implements Runnable {
    RTGame game;
    Bitmap framebuffer;
    Thread renderThread = null;
    SurfaceHolder holder;
    volatile boolean running = false;

    public RTFastRenderView(RTGame game, Bitmap framebuffer) {
        super(game);
        this.game = game;
        this.framebuffer = framebuffer;
        this.holder = getHolder();

    }

    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();

    }

    public void run() {
        Rect dstRect = new Rect();
        long startTime = System.nanoTime();
        while (running) {
            if (!holder.getSurface().isValid())
                continue;


            float deltaTime = (System.nanoTime() - startTime) / 10000000.000f;      //천만 = 1e+7
            // 1ns = 1e-9 sec
            // deltatime = 1/100 sec
            startTime = System.nanoTime();

            if (deltaTime > 3.15) {
                deltaTime = (float) 3.15;
            }


            game.getCurrentScreen().update(deltaTime);
            game.getCurrentScreen().paint(deltaTime);

            Canvas canvas = holder.lockCanvas();
            canvas.getClipBounds(dstRect);
            canvas.drawBitmap(framebuffer, null, dstRect, null);
            holder.unlockCanvasAndPost(canvas);

        }
    }

    public void pause() {
        running = false;
        while (true) {
            try {
                renderThread.join();
                break;
            } catch (InterruptedException e) {
                // retry
            }

        }
    }


}