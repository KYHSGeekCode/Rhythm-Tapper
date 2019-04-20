package sma.rhythmtapper.game.models;
import sma.rhythmtapper.framework.*;
import sma.rhythmtapper.game.*;
import static sma.rhythmtapper.game.EnvVar.SIZE_BALL;

public class Tail extends Connector
{
	public Tail(Ball ball1,Ball ball2)
	{
		super(ball1,ball2);
	}
	float[] tailxs;
	@Override
	public void Paint(Graphics g) {	
       	UpdateGhosts();
		MakeTails();
		int x = ghost2x;
		int y = ghost2y;
		for(int i=0;i<tailxs.length;i++)
		{
			//x = tailxs[i];
			//y = 
		}
        g.drawLine(ghost1x, ghost1y, ghost2x, ghost2y,ball1.color,40);
        if(ghost1y > EnvVar.HITBOX_CENTER && ghost2y <EnvVar.HITBOX_CENTER) {
            int helperx = (GameScreen.HITBOX_CENTER - ghost1y) * (ghost2x - ghost1x) / (ghost2y - ghost1y) + ghost1x;
            g.drawImage(Assets.ballHitpoint, (int) (helperx - SIZE_BALL), (int) (EnvVar.HITBOX_CENTER - SIZE_BALL), SIZE_BALL * 2, SIZE_BALL * 2);
        }
        return;
    }
	public void MakeTails()
	{
		final int delta = EnvVar.speed;
		int dy = ghost1y-ghost2y;
		int slices = dy/delta;
		final float dt = dy/slices;
		if(slices<=1)
			return;
		tailxs = new float[slices-1];
		float tt =0;
		for(int i=0; i<slices-1;i++)
		{
			tailxs[i]= Ball.getXfromT(ghost2x,ghost1x,tt);
			tt+= dt;
		}
	}
}
