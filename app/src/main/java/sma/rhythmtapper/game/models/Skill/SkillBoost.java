package sma.rhythmtapper.game.models.Skill;

import java.util.List;

import sma.rhythmtapper.game.models.GameStatusBundle;
import sma.rhythmtapper.game.models.TestResult;

public class SkillBoost extends Skill {
    public SkillBoost(int period, int chance, int duration) {
        super(period, chance, duration);
    }

    @Override
    public String GetName() {
        return "Boost";
    }

    @Override
    public void PreTest(GameStatusBundle bundle) {
        List<Skill> runningSkills=bundle.runningSkills;
        for(Skill skill:runningSkills)
        {
            if(skill instanceof SkillHeal)
            {
                bundle.ApplyDeltaLife(4);
            } else if (skill instanceof  SkillPerfectSupport)
            {
                bundle.testResult= TestResult.PERFECT;
            }
        }
    }

    @Override
    public void PostTest(GameStatusBundle bundle) {
        List<Skill> runningSkills=bundle.runningSkills;
        for(Skill skill:runningSkills) {
            if (skill instanceof SkillScoreUp) {
                if(bundle.testResult.compareTo(TestResult.NICE)>=0)
                    bundle.ApplyScoreBonus(20);
            } else if (skill instanceof SkillComboSupport) {
                if(bundle.testResult.compareTo(TestResult.BAD)>=0)
                    bundle.continueCombo=true;
            } else if(skill instanceof SkillComboUp)
            {
                bundle.ApplyComboBonus(17);
            } else if (skill instanceof SkillDamageGuard)
            {
                bundle.ApplyDeltaLife(1);
            } else if(skill instanceof SkillOverload) {
                if (bundle.testResult.compareTo(TestResult.NICE) >= 0){
                    bundle.ApplyComboBonus(15);
                    bundle.ApplyScoreBonus(21);
                }
                bundle.continueCombo=true;
            }
        }
    }
}
