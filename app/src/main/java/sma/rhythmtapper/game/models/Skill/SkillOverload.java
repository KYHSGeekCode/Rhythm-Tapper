package sma.rhythmtapper.game.models.Skill;

import sma.rhythmtapper.game.models.GameStatusBundle;
import sma.rhythmtapper.game.models.TestResult;

public class SkillOverload extends Skill {

    public SkillOverload(int period, int chance, int duration) {
        super(period, chance, duration);
        isEffective=false;
    }
    boolean isEffective;
    @Override
    public void Start(GameStatusBundle bundle) {
        if(bundle.life>15) {
            bundle.Damage(15);
            isEffective=true;
        }
    }

    @Override
    public void PostTest(GameStatusBundle bundle) {
        if(isEffective) {
            if (bundle.testResult.compareTo(TestResult.BAD) >= 0) {
                bundle.continueCombo = true;
            }
            if (bundle.testResult.compareTo(TestResult.GREAT) >= 0) {
                bundle.ApplyScoreBonus(18);
            }
            bundle.ApplyComboBonus(13);
        }
    }

    @Override
    public String GetName() {
        return "Overload";
    }

	@Override
	public boolean isAffectLife()
	{
		// TODO: Implement this method
		return true;
	}

	@Override
	public boolean isAffectScore()
	{
		// TODO: Implement this method
		return true;
	}

	@Override
	public boolean isAffectComboBonus()
	{
		// TODO: Implement this method
		return true;
	}
}
