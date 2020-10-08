package sma.rhythmtapper.game.models;

import java.util.ArrayList;
import java.util.List;

import sma.rhythmtapper.game.models.Skill.Skill;

//Controlled by the Deck class
public class GameStatusBundle {
    public int life;
    public int score;
    public TestResult testResult;
    public int combo;
    public int comboBonus;
    public int scoreBonus;
    public boolean continueCombo;
    public boolean shouldDamage;
    public int totalLife;
    int deltaLife;

    public void ApplyScoreBonus(int bonus) {
        scoreBonus = Math.max(scoreBonus, bonus);
    }

    public void ApplyComboBonus(int bonus) {
        comboBonus = Math.max(comboBonus, bonus);
    }

    public void ApplyDeltaLife(int delta) {
        if (deltaLife >= 0) {
            if (delta > 0) {
                deltaLife = Math.max(delta, deltaLife);
            } else {
                deltaLife += delta;
            }
        } else {
            if (delta > 0) {
                deltaLife += delta;
            } else {
                deltaLife = Math.min(delta, deltaLife);
            }
        }
    }

    public void Damage(int amount) {
        if (!shouldDamage && amount > 0)
            return;
        life = Math.min(Math.max(life - amount, 0), totalLife * 2);
    }

    public GameStatusBundle(Deck deck) {
        this.totalLife = deck.life;
        this.life = totalLife;
        score = 0;
        scoreBonus = 0;
        combo = 0;
        comboBonus = 0;
        continueCombo = true;
        shouldDamage = true;
        testResult = TestResult.NICE;
    }

    public boolean isDead() {
        return life <= 0;
    }

    public List<Skill> runningSkills = new ArrayList<>();
}
