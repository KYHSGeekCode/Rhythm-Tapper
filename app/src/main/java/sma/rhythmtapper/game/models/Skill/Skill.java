package sma.rhythmtapper.game.models.Skill;

import sma.rhythmtapper.game.models.GameStatusBundle;
import sma.rhythmtapper.game.models.TestResult;

public abstract class Skill {
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
    public static final Skill nullSkill=new Skill(100,0,1){
        @Override
        public String GetName() {
            return "";
        }
    };

    public abstract String GetName();
}
