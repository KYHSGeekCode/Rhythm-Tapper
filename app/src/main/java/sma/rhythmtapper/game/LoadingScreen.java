package sma.rhythmtapper.game;

import android.os.*;
import android.util.Log;

import java.io.*;
import sma.rhythmtapper.framework.*;
import sma.rhythmtapper.game.models.*;
import sma.rhythmtapper.game.models.Skill.*;
import sma.rhythmtapper.models.*;


public class LoadingScreen extends Screen {
    private Difficulty _diff;
    private static final String IMAGE_PATH = "img/";
    private static final String SOUND_EFFECTS_PATH = "audio/";
    private static final String MUSIC_PATH = "music/";
    private static final String TAG = "LoadingScreen";

    public LoadingScreen(Game game, Difficulty difficulty) {
        super(game);
        this._diff = difficulty;
    }


    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();

        Assets.background = g.newImage(IMAGE_PATH + "background.png", Graphics.ImageFormat.RGB565);
        Assets.gameover = g.newImage(IMAGE_PATH + "gameover.png", Graphics.ImageFormat.RGB565);
        Assets.pause = g.newImage(IMAGE_PATH + "pause.png", Graphics.ImageFormat.RGB565);
        Assets.ballNormal = g.newImage(IMAGE_PATH + "ball_normal.png", Graphics.ImageFormat.RGB565);
        Assets.ballMultiplier = g.newImage(IMAGE_PATH + "ball_multiplier.png", Graphics.ImageFormat.RGB565);
        Assets.ballOneUp = g.newImage(IMAGE_PATH + "ball_oneup.png", Graphics.ImageFormat.RGB565);
        Assets.ballSpeeder = g.newImage(IMAGE_PATH + "ball_speeder.png", Graphics.ImageFormat.RGB565);
        Assets.ballBomb = g.newImage(IMAGE_PATH + "ball_bomb.png", Graphics.ImageFormat.RGB565);
        Assets.explosion = g.newImage(IMAGE_PATH + "explosion.png", Graphics.ImageFormat.RGB565);
        Assets.explosionBright = g.newImage(IMAGE_PATH + "explosion_bright.png", Graphics.ImageFormat.RGB565);
        Assets.ballSkull = g.newImage(IMAGE_PATH + "skull-ball-icon.png", Graphics.ImageFormat.RGB565);
        Assets.sirens = g.newImage(IMAGE_PATH + "sirens.png", Graphics.ImageFormat.RGB565);
        Assets.ballHitpoint = g.newImage(IMAGE_PATH + "ball_hitpoint.png",Graphics.ImageFormat.RGB565);
        Assets.ballFlickLeft = g.newImage(IMAGE_PATH + "ball_flickleft.png",Graphics.ImageFormat.RGB565);
        Assets.ballFlickRight = g.newImage(IMAGE_PATH + "ball_flickright.png",Graphics.ImageFormat.RGB565);

        Assets.soundClick = game.getAudio().createSound(SOUND_EFFECTS_PATH + "sound_guiclick.ogg");
        Assets.soundExplosion = game.getAudio().createSound(SOUND_EFFECTS_PATH + "sound_explosion.ogg");
        Assets.soundCreepyLaugh = game.getAudio().createSound(SOUND_EFFECTS_PATH + "sound_creepy_laugh.mp3");
        Assets.soundFlickOK= game.getAudio().createSound(SOUND_EFFECTS_PATH + "sound_flick_ok.ogg");
        Assets.soundMiss = game.getAudio().createSound(SOUND_EFFECTS_PATH + "sound_miss.ogg");
        Assets.musicTrack = game.getFileAudio().createMusic(_diff.getMusic());
        Log.d(TAG, "musictrack"+Assets.musicTrack);
        //Deck deck=new Deck();
        
        game.setScreen(new GameScreen(game, _diff));
    }
    @Override
    public void paint(float deltaTime) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {

    }
}
