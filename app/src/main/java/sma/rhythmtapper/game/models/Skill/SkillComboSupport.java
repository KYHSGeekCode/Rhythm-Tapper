package sma.rhythmtapper.game.models.Skill;

import sma.rhythmtapper.game.models.GameStatusBundle;
import sma.rhythmtapper.game.models.TestResult;

public class SkillComboSupport extends Skill {
    public SkillComboSupport(int period, int chance, int duration) {
        super(period, chance, duration);
    }

    @Override
    public String GetName() {
        return "Combo Support";
    }

    @Override
    public void PostTest(GameStatusBundle bundle) {
        if(bundle.testResult.compareTo(TestResult.NICE)>=0)
        {
            bundle.continueCombo = true;
        }
    }
}
