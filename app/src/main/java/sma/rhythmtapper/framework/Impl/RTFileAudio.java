package sma.rhythmtapper.framework.Impl;

import android.app.Activity;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.File;

import sma.rhythmtapper.framework.Audio;
import sma.rhythmtapper.framework.Music;
import sma.rhythmtapper.framework.Sound;

public class RTFileAudio implements Audio {

    private AssetManager assets;
    private SoundPool soundPool;

    public RTFileAudio(Activity activity) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.assets = activity.getAssets();
        this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
    }

    @Override
    public Music createMusic(String filename) {
        try {
            return new RTMusic(new File(filename));
        } catch (RuntimeException e) {
            throw new RuntimeException("Couldn't load music '" + filename + "'");
        }
    }

    @Override
    public Sound createSound(String filename) {
        try {
            int soundId = soundPool.load(filename, 0);
            return new RTSound(soundPool, soundId);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't load sound '" + filename + "'");
        }
    }
}
