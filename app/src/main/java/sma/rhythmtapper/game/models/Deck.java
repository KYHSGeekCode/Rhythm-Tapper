package sma.rhythmtapper.game.models;

import java.io.*;
import java.util.*;
import sma.rhythmtapper.game.models.Skill.*;

public class Deck implements Serializable{
    Card[] cards= new Card[5];
    int[] chances=new int [5];
    int visual;
    int vocal;
    int dance;
    int totalAppeal;
    int life;
    Random random;
    GameStatusBundle bundle;

    GameResult result;

    public void SetResult(GameResult result)
    {
        this.result=result;
    }

    public Deck()
    {
        this.random=new Random();
    }
    //i must be below 5
    public void SetCard(int i, Card card)
    {
        cards[i]=card;
    }
    //FInishes calcuation of appeals and probs.
    public void ApplyCenterSkill()
    {
        dance=0;
        vocal=0;
        visual=0;
        life=0;
        for(int i=0;i<5;i++)
        {
            chances[i]=cards[i].skill.chance;
        }
        CenterSkill cs=cards[3].centerSkill;
        if(cs.condition== CenterSkill.Condition.TRICOLOR)
        {
            boolean hasCool=false;
            boolean hasCute=false;
            boolean hasPassion=false;
            for(int i=0;i<5;i++)
            {
                if(cards[i].colorType==ColorType.CUTE)
                {
                    hasCute=true;
                    continue;
                }
                if(cards[i].colorType==ColorType.COOL)
                {
                    hasCool=true;
                    continue;
                }
                if(cards[i].colorType==ColorType.PASSION) {
                    hasPassion = true;
                    continue;
                }
            }
            if(hasCool&&hasCute&&hasPassion)
            {
                for(int i=0;i<5;i++)
                {
                    switch(cs.targetAppeal)
                    {
                        case ANY:
                            visual+=cards[i].visual*(1+cs.amount/100.0f);
                            vocal+=cards[i].vocal*(1+cs.amount/100.0f);
                            dance+=cards[i].dance*(1+cs.amount/100.0f);
                            break;
                        case VISUAL:
                            visual+=cards[i].visual*(1+cs.amount/100.0f);
                            break;
                        case VOCAL:
                            vocal+=cards[i].vocal*(1+cs.amount/100.0f);
                            break;
                        case DANCE:
                            dance+=cards[i].dance*(1+cs.amount/100.0f);
                            break;
                        case SKILL_CHANCE:
                            chances[i]=cards[i].skill.chance+cs.amount;
                            break;
                        case LIFE:
                            life+=cards[i].life*(1+cs.amount/100.0f);
                            break;
                        default:
                            break;
                    }
                }
            }
        } else {
            for (int i = 0; i < 5; i++) {
                if (cs.targetType == ColorType.ANY || cs.targetType == cards[i].colorType) {
                    switch (cs.targetAppeal) {
                        case ANY:
                            visual += cards[i].visual * (1 + cs.amount / 100.0f);
                            vocal += cards[i].vocal * (1 + cs.amount / 100.0f);
                            dance += cards[i].dance * (1 + cs.amount / 100.0f);
                            break;
                        case VISUAL:
                            visual += cards[i].visual * (1 + cs.amount / 100.0f);
                            break;
                        case VOCAL:
                            vocal += cards[i].vocal * (1 + cs.amount / 100.0f);
                            break;
                        case DANCE:
                            dance += cards[i].dance * (1 + cs.amount / 100.0f);
                            break;
                        case SKILL_CHANCE:
                            chances[i] = cards[i].skill.chance + cs.amount;
                            break;
                        case LIFE:
                            life += cards[i].life * (1 + cs.amount / 100.0f);
                            break;
                        default:
                            break;
                    }
                } else {
                    visual += cards[i].visual;
                    vocal += cards[i].vocal;
                    dance += cards[i].dance;
                    life += cards[i].life;
                }
            }
        }
        totalAppeal=visual+vocal+dance;
    }
    public void StartGame(GameStatusBundle bundle)
    {
        this.bundle=bundle;
    }
    //몇 밀리 초가 지났는가
    //1000000이 되면 1초가 지났단 뜻이다.
    float[] periodTimer=new float[5];
    boolean[] bSkillOn=new boolean[5];
    boolean [] bSkillCheckedNow= new boolean[5];
    public void Update(float deltaTime)
    {
        ////deltatime is nanosec/10^7 which means deltatime is 1, then 100 nanoseconds elapsed.
        ////1 sec elapsed-> 1000000 is deltatime
        for(int i=0;i<5;i++)
        {
            periodTimer[i]+=deltaTime * 0.01f;
            int sec=(int)(periodTimer[i]);
            if(sec>cards[i].skill.period/*&&bSkillCheckedNow[i]==false*/)
            {
                //bSkillCheckedNow[i]=true;
                if(random.nextInt()%100<chances[i])
                {
                    //start applying skill
                    bSkillOn[i]=true;
                    cards[i].skill.Start(bundle);
                    bundle.runningSkills.add(cards[i].skill);
                }
            }
            if(bSkillOn[i]&&(sec>cards[i].skill.period+cards[i].skill.duration))
            {
                periodTimer[i]=0;
                //finish applying skill
                bSkillOn[i]=false;
                //cards[i].skill.End();
                //bSkillCheckedNow[i]=false;
                bundle.runningSkills.remove(cards[i].skill);
            }
        }
    }
    public void Apply(GameStatusBundle bundle)
    {
        //how to apply combo bonus?
        //score bonus?
        int basicScore=totalAppeal/100;
        //bundle.testResult = TestResult.PERFECT;
        if(bundle.testResult.compareTo(TestResult.NICE)<=0)
            bundle.continueCombo=false;
        else
            bundle.continueCombo=true;

        if(bundle.testResult.compareTo(TestResult.BAD)<=0)
            bundle.shouldDamage=true;
        bundle.shouldDamage = false;
        for(int i=0;i<5;i++)
        {
            if(bSkillOn[i])
                cards[i].skill.PreTest(bundle);
        }
        for(int i=0;i<5;i++)
        {
            if(bSkillOn[i])
                cards[i].skill.PostTest(bundle);
        }
        basicScore*=GetScoreAmpByTestResult(bundle.testResult);
        int totalBonus=bundle.scoreBonus+bundle.comboBonus*bundle.combo/50;
        basicScore*=(1+totalBonus/100.0f);
        if(bundle.continueCombo==false)
        {
            result.maxcombo=Math.max(result.maxcombo,bundle.combo);
            bundle.combo=0;
			bundle.continueCombo=true;
        } else {
            bundle.combo++;
        }
        bundle.score+=basicScore;
        bundle.Damage(-bundle.deltaLife);
        bundle.deltaLife=0;
        if(bundle.testResult==TestResult.BAD)
        {
            bundle.Damage(15);
        } else if (bundle.testResult==TestResult.MISS)
            bundle.Damage(18);
        switch (bundle.testResult)
        {
            case PERFECT:
                result.perfect++;
                break;
            case GREAT:
                result.great++;
                break;
            case NICE:
                result.nice++;
                break;
            case BAD:
                result.bad++;
                break;
            case MISS:
                result.miss++;
                break;
            default:
                break;
        }
        result.score=bundle.score;
    }

    private float GetScoreAmpByTestResult(TestResult testResult) {
        switch (testResult)
        {
            case PERFECT:
                return 1;
            case GREAT:
                return 0.75f;
            case NICE:
                return 0.5f;
            case BAD:
                return 0.3f;
            case MISS:
            default:
                //what??
                return 0.0f;
        }
    }

	public List<Skill> getActivatedSkills()
	{
		List<Skill> sk= new ArrayList<>();
		for(int i=0;i<bSkillOn.length;i++)
        {
            if(bSkillOn[i])
            {
                sk.add (cards[i].skill);
            }
        }
		return sk;
	}
	
	
    public String GetActivatedSkillNames() {
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<5;i++)
        {
            if(bSkillOn[i])
            {
                sb.append (" | ");
                sb.append(cards[i].skill.GetName());
            }
        }
        sb.append(" | ");
        return sb.toString();
    }
}
