package sma.rhythmtapper.framework;

import android.content.Context;
import android.os.Vibrator;

import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * Created by Peter on 23.01.2017.
 */

public interface Game {
    Audio getAudio();

    Audio getFileAudio();

    Input getInput();

    FileIO getFileIO();

    Graphics getGraphics();

    Vibrator getVibrator();

    void setScreen(Screen screen);

    Screen getCurrentScreen();

    Screen getInitScreen();

    int getScreenX();

    int getScreenY();

    void goToActivity(Class<?> activity);

    FFmpegMediaMetadataRetriever createVideo(String video);

    Context getContext();
}
