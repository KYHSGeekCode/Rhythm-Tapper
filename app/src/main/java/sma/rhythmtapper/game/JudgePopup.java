package sma.rhythmtapper.game;
import sma.rhythmtapper.framework.*;
import sma.rhythmtapper.game.models.*;
import android.graphics.*;

public class JudgePopup
{
	TestResult tr;
	int tick=0;
	int bkColor;
	int fgColor;
	Paint paint = new Paint();
	int alpha;
	public JudgePopup()
	{
		paint.setTextSize(90);
		paint.setStyle(Paint.Style.STROKE);
	}
	public void Show(TestResult tr)
	{
		this.tr = tr;
		tick = 500;
		bkColor = tr.getBkColor();
		fgColor = tr.getFgColor();
		//paint.setColor(tr.getColor());
	}
	public void Update(float dtsec)
	{
		tick -= (int)(dtsec*1000);
		alpha = (int)(tick*255/500.0f);
	}
	public void Paint(Graphics g)
	{
		if(tick>0)
		{
			paint.setColor(bkColor);
			paint.setAlpha(alpha);
			paint.setStrokeWidth(13);
			g.drawString(tr.name(),(int)EnvVar.gameWidth/2,(int)(EnvVar.gameHeight*0.5f),paint,true);
			paint.setColor(fgColor);
			paint.setAlpha(alpha);
			paint.setStrokeWidth(8);
			g.drawString(tr.name(),(int)EnvVar.gameWidth/2,(int)(EnvVar.gameHeight*0.5f),paint,true);
		}
	}
}
