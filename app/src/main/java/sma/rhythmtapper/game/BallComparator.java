package sma.rhythmtapper.game;

import java.io.Serializable;
import java.util.Comparator;

import sma.rhythmtapper.game.models.Ball;

public class BallComparator implements Serializable, Comparator<Ball> {
    @Override
    public int compare(Ball ball, Ball ball2) {
        float d = ball.time - ball2.time;
        return d > 0 ? 1 : d < 0 ? -1 : 0;
    }
}
