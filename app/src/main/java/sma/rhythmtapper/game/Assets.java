package sma.rhythmtapper.game;

import sma.rhythmtapper.framework.Image;
import sma.rhythmtapper.framework.Music;
import sma.rhythmtapper.framework.Sound;
import wseemann.media.FFmpegMediaMetadataRetriever;

public class Assets {
    // Graphics
    static Image background;
    static Image gameover;
    static Image pause;
    static Image ballNormal;
    static Image ballMultiplier;
    static Image ballOneUp;
    static Image ballSpeeder;
    static Image ballBomb;
    static Image explosion;
    static Image ballSkull;
    static Image explosionBright;
    static Image sirens;
    public static Image ballHitpoint;

    static Image ballFlickLeft;
    static Image ballFlickRight;
    // Audio
    static Sound soundClick;
    static Sound soundExplosion;
    static Sound soundCreepyLaugh;
    static Sound soundFlickOK;
    static Sound soundMiss;

    static Music musicTrack;
    static FFmpegMediaMetadataRetriever videoExtractor;
}