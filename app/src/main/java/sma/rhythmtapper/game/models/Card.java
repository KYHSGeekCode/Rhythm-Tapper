package sma.rhythmtapper.game.models;

import sma.rhythmtapper.game.models.Skill.Skill;

public class Card {
    int vocal;
    int visual;
    int dance;
    CenterSkill centerSkill;
    Skill skill;
    ColorType colorType;
    int life;
    public Card(ColorType colorType, int voc, int vis, int dan, int life, CenterSkill cs, Skill sk)
    {
        this.colorType=colorType;
        this.vocal=voc;
        this.visual=vis;
        this.dance=dan;
        this.life=life;
        this.centerSkill=cs;
        this.skill=sk;
    }
    public static final Card nullCard=new Card(ColorType.ANY, 0,0,0,0,CenterSkill.nullCenterSkill,Skill.nullSkill);
}