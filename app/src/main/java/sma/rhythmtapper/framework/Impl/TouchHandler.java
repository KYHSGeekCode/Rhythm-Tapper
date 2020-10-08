package sma.rhythmtapper.framework.Impl;

import android.view.View.OnTouchListener;

import java.util.List;

import sma.rhythmtapper.framework.Input;

public interface TouchHandler extends OnTouchListener {
    boolean isTouchDown(int pointer);

    int getTouchX(int pointer);

    int getTouchY(int pointer);

    List<Input.TouchEvent> getTouchEvents();
}