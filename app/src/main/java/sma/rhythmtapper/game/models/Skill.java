package sma.rhythmtapper.game.models;

public abstract class Skill {
    int period;
    int chance;
    int duration;

    public Skill(int period, int chance, int duration)
    {
        this.period=period;
        this.chance=chance;
        this.duration=duration;
    }
    public void Start()
    {

    }
    public void PostTest(GameStatusBundle bundle)
    {

    }
    public void PreTest(GameStatusBundle bundle)
    {

    }
    public static final Skill Heal1=new Skill(7,40,4){
        @Override
        public void PostTest(GameStatusBundle bundle) {
            if(bundle.testResult==TestResult.PERFECT)
            {
                bundle.life+=3;
            }
        }
    };
    public static final Skill nullSkill=new Skill(100,0,1){
    };
}
