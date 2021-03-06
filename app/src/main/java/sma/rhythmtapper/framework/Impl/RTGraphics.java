package sma.rhythmtapper.framework.Impl;


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;

import java.io.IOException;
import java.io.InputStream;

import sma.rhythmtapper.framework.Graphics;
import sma.rhythmtapper.framework.Image;

public class RTGraphics implements Graphics {

    private AssetManager assets;
    private Bitmap frameBuffer;
    private Canvas canvas;
    private Paint paint;
    private Rect srcRect = new Rect();
    private Rect dstRect = new Rect();

    private Path path = new Path();

    public RTGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
        paint.setStrokeWidth(10);
    }

    @Override
    public Image newImage(String fileName, ImageFormat format) {
        Config config = null;
        if (format == ImageFormat.RGB565)
            config = Config.RGB_565;
        else if (format == ImageFormat.ARGB4444)
            config = Config.ARGB_4444;
        else
            config = Config.ARGB_8888;

        Options options = new Options();
        options.inPreferredConfig = config;


        InputStream in = null;
        Bitmap bitmap = null;
        try {
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '"
                        + fileName + "'");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '"
                    + fileName + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        if (bitmap.getConfig() == Config.RGB_565)
            format = ImageFormat.RGB565;
        else if (bitmap.getConfig() == Config.ARGB_4444)
            format = ImageFormat.ARGB4444;
        else
            format = ImageFormat.ARGB8888;

        return new RTImage(bitmap, format);
    }

    @Override
    public void clearScreen(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }

    @Override
    public void DrawPath(Path path) {
        canvas.drawPath(path, paint);
    }
    @Override
    public void DrawPath(Path path, Paint thePaint) {
        canvas.drawPath(path, thePaint);
    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color, int stroke) {
        paint.setColor(color);
        paint.setStrokeWidth(stroke);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        drawLine(x, y, x2, y2, color, 10);
    }

    public void drawLinear(int x, int y, int x2, int y2) {
        drawLinear(x, y, x2, y2, Color.WHITE);
    }

    @Override
    public void drawLinear(int x, int y, int x2, int y2, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        path.moveTo(x - 30, y - 30);
        path.lineTo(x2 - 30, y2 + 30);
        path.lineTo(x2 + 30, y2 + 30);
        path.lineTo(x + 30, y - 30);
        path.lineTo(x - 30, y - 30);
        path.close();
        canvas.drawPath(path, paint);
        path.reset();
    }


    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, Paint paint) {
        //paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }

    @Override
    public void drawARGB(int a, int r, int g, int b) {
        paint.setStyle(Style.FILL);
        canvas.drawARGB(a, r, g, b);
    }

    @Override
    public void drawString(String text, int x, int y, Paint paint, boolean center) {
        if (center == true) {
            int dx = (int) (paint.measureText(text) / 2);
            x -= dx;
        }
        canvas.drawText(text, x, y, paint);
    }


    @Override
    public void drawString(String text, int x, int y, Paint paint) {
        canvas.drawText(text, x, y, paint);
    }


    public void drawImage(Image Image, int x, int y, int srcX, int srcY,
                          int srcWidth, int srcHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth;
        dstRect.bottom = y + srcHeight;

        canvas.drawBitmap(((RTImage) Image).bitmap, srcRect, dstRect,
                null);
    }

    @Override
    public void drawImage(Image Image, int x, int y, int w, int h) {
        Rect rect = new Rect(x, y, x + w, y + h);
        Bitmap bit = ((RTImage) Image).bitmap;
        Rect srcRect = new Rect(0, 0, bit.getHeight(), bit.getWidth());
        canvas.drawBitmap(bit, srcRect, rect, null);
        //canvas.drawBitmap(((RTImage)Image).bitmap, x, y, null);
    }

    @Override
    public void drawImage(Image Image, int x, int y) {
        canvas.drawBitmap(((RTImage) Image).bitmap, x, y, null);
    }

    @Override
    public void drawImage(Bitmap bitmap, int x, int y) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public void drawScaledImage(Image Image, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight) {


        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;


        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + width;
        dstRect.bottom = y + height;


        canvas.drawBitmap(((RTImage) Image).bitmap, srcRect, dstRect, null);

    }

    public void drawScaledImage(Bitmap bitmap, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight) {


        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;


        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + width;
        dstRect.bottom = y + height;


        canvas.drawBitmap(bitmap, srcRect, dstRect, null);

    }

    @Override
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return frameBuffer.getHeight();
    }
}
