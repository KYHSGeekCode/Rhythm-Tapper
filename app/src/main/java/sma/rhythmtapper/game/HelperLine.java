package sma.rhythmtapper.game;

import android.graphics.Color;

import java.io.Serializable;

import sma.rhythmtapper.framework.Graphics;
import sma.rhythmtapper.game.models.Ball;

public class HelperLine implements Serializable {
    public void Paint(Graphics g) {
        g.drawLine(ball1.x, ball1.y, ball2.x, ball2.y, Color.LTGRAY, 10);
    }

    public HelperLine(Ball ball1, Ball ball2) {
        this.ball1 = ball1;
        this.ball2 = ball2;
    }

    Ball ball1, ball2;
}
