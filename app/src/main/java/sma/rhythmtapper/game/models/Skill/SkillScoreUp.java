package sma.rhythmtapper.game.models.Skill;

import sma.rhythmtapper.game.models.GameStatusBundle;
import sma.rhythmtapper.game.models.TestResult;

public class SkillScoreUp extends Skill {
    public SkillScoreUp(int period, int chance, int duration) {
        super(period, chance, duration);
    }

    @Override
    public String GetName() {
        return "Score up";
    }

    @Override
    public void PostTest(GameStatusBundle bundle) {
        if(bundle.testResult.compareTo(TestResult.GREAT)>=0)
        {
            bundle.ApplyScoreBonus(17);
        }
    }

	@Override
	public boolean isAffectScore()
	{
		// TODO: Implement this method
		return true;
	}
	
}
