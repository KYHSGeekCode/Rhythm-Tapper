package sma.rhythmtapper.framework.Impl;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import sma.rhythmtapper.framework.Graphics.ImageFormat;
import sma.rhythmtapper.framework.Image;

public class RTImage implements Image {
    Bitmap bitmap;
    ImageFormat format;

    int color;

    public RTImage(Bitmap bitmap, ImageFormat format) {
        this.bitmap = bitmap;
        this.format = format;
    }

    public Bitmap ChangeColor(int color) {
        if (this.color == color)
            return bitmap;
        this.color = color;
        Bitmap sourceBitmap = bitmap;
        float r = Color.red(color);
        float g = Color.green(color);
        float b = Color.blue(color);

        float[] colorTransform = {
                r / 255, 0, 0, 0, 0,
                0, g / 255, 0, 0, 0,
                0, 0, b / 255, 0, 0,
                0, 0, 0, 1f, 0};

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f); //Remove Colour
        colorMatrix.set(colorTransform); //Apply the Red

        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);

        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());

        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, paint);
        bitmap.recycle();
        bitmap = resultBitmap;
        return bitmap;
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public ImageFormat getFormat() {
        return format;
    }

    @Override
    public void dispose() {
        bitmap.recycle();
    }
}