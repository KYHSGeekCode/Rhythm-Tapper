package sma.rhythmtapper.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;

import wseemann.media.FFmpegMediaMetadataRetriever;

import static android.os.Environment.isExternalStorageRemovable;

public class MVProvider extends Thread {
    private final int sec;
    private final FFmpegMediaMetadataRetriever videoReader;
    public static final int FRAMERATE = 3;
    private final int frame;
    private final int _gameWidth;
    private final int _gameHeight;
    private final File cacheDir;
    private Bitmap[] frames;
    private static final String TAG = "MVProvider";
    private int alives = 0;
    private boolean initialized;
    int lastindex = 0;

    public MVProvider(Context context, int sec, FFmpegMediaMetadataRetriever videoReader, int width, int height) {
        this.sec = sec;
        this.videoReader = videoReader;
        this.frame = FRAMERATE * sec / 1000;
        this.frames = new Bitmap[frame];
        this._gameWidth = width;
        this._gameHeight = height;
        // Initialize memory cache
        // Initialize disk cache on background thread
        cacheDir = getDiskCacheDir(context, DISK_CACHE_SUBDIR);
        initialized = false;
    }

    @Override
    public void run() {
        if (videoReader == null)
            return;
        cacheDir.mkdirs();
       /* try {
            for (int i = 0; i < frames.length; i++) {
                Log.d(TAG, "i="+i);
                Bitmap frame = videoReader.getScaledFrameAtTime((long) (i * 1000000.0f/FRAMERATE), FFmpegMediaMetadataRetriever.OPTION_CLOSEST, _gameWidth, _gameHeight);
                FileOutputStream out = new FileOutputStream(new File(cacheDir,String.valueOf(i)));
                frame.compress(Bitmap.CompressFormat.PNG, 100, out);
                if(i>frames.length/2)
                    initialized = true;
            }
        }catch (Exception e)
        {
            Log.e(TAG,"Error bitmap cache creating",e);
            Log.w(TAG,"error?",e);
        }*/
        //initialized = true;
        for (int i = 0; i < frames.length; i++) {
            if (lastindex > i)
                i = lastindex + FRAMERATE;
            if (i >= frames.length)
                break;
            Bitmap frame = videoReader.getScaledFrameAtTime((long) (i * 1000000.0f / FRAMERATE), FFmpegMediaMetadataRetriever.OPTION_CLOSEST, _gameWidth, _gameHeight);
            //Bitmap frame = BitmapFactory.decodeFile(new File(cacheDir,String.valueOf(i)).getPath());
            frames[i] = frame;
            alives++;
            while (alives > 80) {
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    public Bitmap getBitmap(int index) {
        lastindex = index;
        alives--;
        if (index < frames.length)
            return frames[index];
        return null;
    }

    private static final String DISK_CACHE_SUBDIR = "thumbnails";

    // Creates a unique subdirectory of the designated app cache directory. Tries to use external
    // but if not mounted, falls back on internal storage.
    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? context.getExternalCacheDir().getPath() :
                        context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }

    public boolean isInitialized() {
        return true;
//        return initialized;
    }

    public void recycle(int oldindex) {
        if (oldindex < frames.length)
            frames[oldindex].recycle();
    }

    public void release() {
        videoReader.release();
    }
}
