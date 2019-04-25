package sma.rhythmtapper.game.models.Skill;

import sma.rhythmtapper.game.models.GameStatusBundle;

public class SkillComboUp extends Skill {
    @Override
    public String GetName() {
        return "Combo up";
    }

    public SkillComboUp(int period, int chance, int duration) {
        super(period, chance, duration);
    }

    @Override
    public void PostTest(GameStatusBundle bundle) {
        bundle.ApplyComboBonus(15);
    }

	@Override
	public boolean isAffectComboBonus()
	{
		// TODO: Implement this method
		return true;
	}
}
