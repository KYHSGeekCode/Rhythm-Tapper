package sma.rhythmtapper.framework;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Peter on 23.01.2017.
 */
public interface Graphics {
    enum ImageFormat {
        ARGB8888, ARGB4444, RGB565
    }

    Image newImage(String fileName, ImageFormat format);

    void clearScreen(int color);

    void DrawPath(Path path);

    void drawLine(int x, int y, int x2, int y2, int color, int stroke);

    void drawLine(int x, int y, int x2, int y2, int color);

    void drawLinear(int x, int y, int x2, int y2);

    void drawLinear(int x, int y, int x2, int y2, int color);

    void drawRect(int x, int y, int width, int height, int color);

    void drawRect(int x, int y, int width, int height, Paint paint);

    void drawImage(Image image, int x, int y, int srcX, int srcY,
                   int srcWidth, int srcHeight);


    void drawImage(Image Image, int x, int y);

    void drawImage(Bitmap Image, int x, int y);

    void drawImage(Image Image, int x, int y, int w, int h);

    void drawScaledImage(Image Image, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight);

    void drawScaledImage(Bitmap Image, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight);

    void drawString(String text, int x, int y, Paint paint);

    void drawString(String text, int x, int y, Paint paint, boolean center);


    int getWidth();

    int getHeight();

    void drawARGB(int i, int j, int k, int l);
}
