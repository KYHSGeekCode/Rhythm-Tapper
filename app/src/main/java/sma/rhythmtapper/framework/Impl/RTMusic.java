package sma.rhythmtapper.framework.Impl;


import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import sma.rhythmtapper.framework.Music;

public class RTMusic implements Music, OnCompletionListener, OnSeekCompleteListener, OnPreparedListener, OnVideoSizeChangedListener {
    private MediaPlayer mediaPlayer;
    private boolean isPrepared = false;
    private static String TAG = "RTMusic";

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public RTMusic(File file) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(file.getPath());
            Constructor();
        } catch (Exception e) {
            throw new RuntimeException("Couldn't load music");
        }
    }

    public RTMusic(AssetFileDescriptor assetDescriptor) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(),
                    assetDescriptor.getStartOffset(),
                    assetDescriptor.getLength());
            Constructor();
        } catch (Exception e) {
            throw new RuntimeException("Couldn't load music");
        }
    }

    private void Constructor() {
        try {
            mediaPlayer.prepare();
            isPrepared = true;
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnSeekCompleteListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnVideoSizeChangedListener(this);
            Log.v(TAG, "music prepared");
        } catch (Exception e) {
            throw new RuntimeException("Couldn't load music");
        }
    }

    @Override
    public void dispose() {

        if (this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
        }
        this.mediaPlayer.release();
    }

    @Override
    public boolean isLooping() {
        return mediaPlayer.isLooping();
    }

    @Override
    public boolean isPlaying() {
        return this.mediaPlayer.isPlaying();
    }

    @Override
    public boolean isStopped() {
        return !isPrepared;
    }

    @Override
    public void pause() {
        if (this.mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }


    @Override
    public void play() {
        if (this.mediaPlayer.isPlaying())
            return;

        try {
            synchronized (this) {
                if (!isPrepared)
                    mediaPlayer.prepare();
                mediaPlayer.start();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setLooping(boolean isLooping) {
        mediaPlayer.setLooping(isLooping);
    }

    @Override
    public void setVolume(float volume) {
        mediaPlayer.setVolume(volume, volume);
    }

    @Override
    public void stop() {
        if (this.mediaPlayer.isPlaying() == true) {
            this.mediaPlayer.stop();

            synchronized (this) {
                isPrepared = false;
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer player) {
        synchronized (this) {
            isPrepared = false;
        }
    }

    @Override
    public void seekBegin() {
        mediaPlayer.seekTo(0);

    }


    @Override
    public void onPrepared(MediaPlayer player) {
        // TODO Auto-generated method stub
        synchronized (this) {
            isPrepared = true;
        }

    }

    @Override
    public void onSeekComplete(MediaPlayer player) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onVideoSizeChanged(MediaPlayer player, int width, int height) {
        // TODO Auto-generated method stub

    }
}