package sma.rhythmtapper.game.models.Skill;

import sma.rhythmtapper.game.models.GameStatusBundle;

public class SkillDamageGuard extends Skill {
    @Override
    public String GetName() {
        return "Damage Guard";
    }

    public SkillDamageGuard(int period, int chance, int duration) {
        super(period, chance, duration);
    }

    @Override
    public void PreTest(GameStatusBundle bundle) {
        bundle.shouldDamage = false;
    }

    @Override
    public void Finish(GameStatusBundle bundle) {
        bundle.shouldDamage = true;
    }

    @Override
    public boolean isAffectLife() {
        // TODO: Implement this method
        return true;
    }

}
