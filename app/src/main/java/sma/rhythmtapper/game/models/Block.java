package sma.rhythmtapper.game.models;

import java.util.ArrayList;
import java.util.List;

public class Block {
    List<List<Ball>> balls=new ArrayList<>();
    public Block()
    {
        for(int i=0;i<5;i++)
        {
            balls.add(new ArrayList<Ball>());
        }
    }
    //lane : 1~5
    public void AddBall(Ball ball, int lane)
    {
        balls.get(lane-1).add(ball);
    }
}
