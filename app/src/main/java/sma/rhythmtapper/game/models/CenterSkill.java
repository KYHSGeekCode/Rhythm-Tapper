package sma.rhythmtapper.game.models;
import java.io.*;

public class CenterSkill implements Serializable {
    ColorType targetType;
    AppealType targetAppeal;
    public enum Condition
    {
        ANY,
        TRICOLOR,
        COOL_PRINCESS,
        CUTE_PRINCESS,
        PASSION_PRINCESS
    }
    Condition condition;
    int amount;
    public CenterSkill(Condition condition,ColorType targetType, AppealType targetAppeal, int amount)
    {
        this.condition=condition;
        this.targetType=targetType;
        this.targetAppeal=targetAppeal;
        this.amount=amount;
    }
    public static final CenterSkill nullCenterSkill=new CenterSkill(Condition.ANY,ColorType.ANY, AppealType.ANY, 0);
}
