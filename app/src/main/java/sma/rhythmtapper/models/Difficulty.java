package sma.rhythmtapper.models;

import java.io.Serializable;

import sma.rhythmtapper.framework.Music;
import sma.rhythmtapper.game.models.Difficulties;

public class Difficulty implements Serializable{

    public static final String EASY_TAG = "easy";
    public static final String MED_TAG = "medium";
    public static final String HARD_TAG = "hard";

    private String _music;
    private String _video;
    private float _spawnInterval;
    private int _ballSpeed;
    private Difficulties _mode;

    public Difficulty(Difficulties _mode, String music,String video, float bpm, int ballSpeed) {
        this._mode = _mode;
        this._music = music;
        this._spawnInterval = 60 / bpm * 100 / 2;
        this._ballSpeed = ballSpeed;
        this._video = video;
    }

    public String getMusic() {
        return _music;
    }
    public String getVideo() {
        return _video;
    }

    public void setMusic(String _music) {
        this._music = _music;
    }

    public void setVideo(String _video) {
        this._video = _video;
    }

    public float getSpawnInterval() {
        return _spawnInterval;
    }

    public void setSpawnInterval(float _spawnInterval) {
        this._spawnInterval = _spawnInterval;
    }

    public int getBallSpeed() {
        return _ballSpeed;
    }

    public void setBallSpeed(int _ballSpeed) {
        this._ballSpeed = _ballSpeed;
    }

    public Difficulties getMode() {
        return _mode;
    }

    public void setMode(Difficulties _mode) {
        this._mode = _mode;
    }
}
