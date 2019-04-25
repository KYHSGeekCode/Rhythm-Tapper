package sma.rhythmtapper.game.models.Skill;

import sma.rhythmtapper.game.models.GameStatusBundle;
import sma.rhythmtapper.game.models.TestResult;
import java.io.*;

public abstract class Skill implements Serializable{
    public int period;
    public int chance;
    public int duration;

    public Skill(int period, int chance, int duration)
    {
        this.period=period;
        this.chance=chance;
        this.duration=duration;
    }
    public void Start(GameStatusBundle bundle)
    {

    }
    public void PostTest(GameStatusBundle bundle)
    {

    }
    public void PreTest(GameStatusBundle bundle)
    {

    }
    public void Finish(GameStatusBundle bundle)
    {

    }
	public boolean isAffectLife()
	{
		return false;
	}
	public boolean isAffectScore()
	{
		return false;
	}
	public boolean isAffectComboBonus()
	{
		return false;
	}

    public static final Skill nullSkill=new Skill(100,0,1){
        @Override
        public String GetName() {
            return "";
        }
    };

    public abstract String GetName();
}
