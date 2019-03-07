package sma.rhythmtapper.game.models.Skill;

import junit.framework.Test;

import sma.rhythmtapper.game.models.GameStatusBundle;
import sma.rhythmtapper.game.models.TestResult;

public class SkillPerfectSupport extends Skill {
    public SkillPerfectSupport(int period, int chance, int duration) {
        super(period, chance, duration);
    }

    @Override
    public String GetName() {
        return "Perfect Support";
    }

    @Override
    public void PreTest(GameStatusBundle bundle) {
        if(bundle.testResult.compareTo(TestResult.BAD)>=0)
        {
            bundle.testResult=TestResult.PERFECT;
            bundle.continueCombo=true;
        }
    }
}
