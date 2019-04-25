package sma.rhythmtapper.game.models;
import android.graphics.*;

public enum TestResult {
    MISS,
    BAD,
    NICE,
    GREAT,
    PERFECT;
	public int getBkColor()
	{
		switch(this)
		{
			case MISS:
				return Color.rgb(0,120,0);
			case BAD:
				return Color.rgb(0,0,120);
			case NICE:
				return Color.rgb(50,50,0);
			case GREAT:
				return Color.RED;
			case PERFECT:
				return Color.rgb(120,0,120);
		}
		return Color.WHITE;
	}
	public int getFgColor()
	{
		switch(this)
		{
			case MISS:
				return Color.GREEN;
			case BAD:
				return Color.BLUE;
			case NICE:
				return Color.YELLOW;
			case GREAT:
				return Color.rgb(255,19,190);
			case PERFECT:
				return Color.MAGENTA;
		}
		return Color.DKGRAY;
	}
}
