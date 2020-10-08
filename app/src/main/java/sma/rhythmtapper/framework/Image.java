package sma.rhythmtapper.framework;

public interface Image {
    int getWidth();

    int getHeight();

    Graphics.ImageFormat getFormat();

    void dispose();
}
