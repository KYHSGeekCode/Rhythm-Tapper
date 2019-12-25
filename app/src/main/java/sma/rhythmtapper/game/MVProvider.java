package sma.rhythmtapper.game;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.Choreographer;

import java.util.concurrent.ArrayBlockingQueue;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class MVProvider extends Thread {
    private final int sec;
    private final FFmpegMediaMetadataRetriever videoReader;
    public static final int FRAMERATE = 3;
    private final int frame;
    private final int _gameWidth;
    private final int _gameHeight;
    private Bitmap[] frames;
    private int sleepDuration = 10;
    private static final String TAG = "MVProvider";
    private int alives= 0;
    public MVProvider(int sec, FFmpegMediaMetadataRetriever videoReader, int width, int height)
    {
        this.sec = sec;
        this.videoReader = videoReader;
        this.frame = FRAMERATE * sec;
        this.frames = new Bitmap[frame];
        this._gameWidth = width;
        this._gameHeight = height;
        this.sleepDuration = Math.max(1000/FRAMERATE - 300,10);
    }
    @Override
    public void run() {
        try {
            for (int i = 0; i < frames.length; i++) {
                Log.d(TAG, "i="+i);
                frames[i] = videoReader.getScaledFrameAtTime((long) (i * 1000000.0f/FRAMERATE), FFmpegMediaMetadataRetriever.OPTION_CLOSEST, _gameWidth, _gameHeight);
                //Thread.sleep(1000/ FRAMERATE - 400);
                alives ++;
                while(alives >100)
                    sleep(1);
            }
        }catch (Exception e)
        {
            Log.w(TAG,"error?",e);
        }
    }

    public Bitmap getBitmap(int index)
    {
        alives--;
        if(index < frames.length)
            return frames[index];
        return null;
    }
}
