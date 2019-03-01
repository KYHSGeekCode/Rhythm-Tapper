package sma.rhythmtapper.game.models.Skill;

import sma.rhythmtapper.game.models.GameStatusBundle;
import sma.rhythmtapper.game.models.TestResult;

public class SkillHeal extends Skill {
    public SkillHeal(int period, int chance, int duration) {
        super(period, chance, duration);
    }

    @Override
    public String GetName() {
        return "Recover";
    }

    @Override
    public void PostTest(GameStatusBundle bundle) {
        if(bundle.testResult== TestResult.PERFECT)
        {
            bundle.ApplyDeltaLife(3);
        }
    }
}
