package sma.rhythmtapper.game.NoteFile;

import java.util.ArrayList;
import java.util.List;

import sma.rhythmtapper.game.models.Ball;

public class Block {

    int bits;
    List<List<Ball>> balls=new ArrayList<>();
    public Block(int bits)
    {
        for(int i=0;i<5;i++)
        {
            balls.add(new ArrayList<Ball>());
        }
        reader=0;
        this.bits=bits;
    }
    //lane : 1~5
    public void AddBall(Ball ball, int lane)
    {
        balls.get(lane-1).add(ball);
    }
    public void AddBalls(Ball [] balls)
    {
        for(int i=0;i<5;i++)
        {
            this.balls.get(i).add(balls[i]);
        }
        if(this.balls.get(0).size()>bits)
            throw new RuntimeException("more notes than that of the #block statement");
    }
    public Ball getSpawnBall(int lane)
    {
        return balls.get(lane).get(reader);
    }
    public boolean NextLine()
    {
        reader++;
        return reader<balls.size();
    }
    int reader;
}
