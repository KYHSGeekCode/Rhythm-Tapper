package sma.rhythmtapper;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import sma.rhythmtapper.game.NoteFile.*;
import sma.rhythmtapper.game.models.*;
import sma.rhythmtapper.game.models.Skill.*;
import sma.rhythmtapper.models.*;

public class ChooseDeckActivity extends Activity {
    Button btStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_deck);
        btStart= (Button) findViewById(R.id.chooseDeckBtStart);
        Intent intent=getIntent();
		final NoteFile nf=(NoteFile) intent.getSerializableExtra("notefile");
        final String path = nf.getPath(); //intent.getStringExtra("Name");
        final String difficulty = intent.getStringExtra("Difficulty");
		final Deck deck=new Deck();
		deck.SetCard(0,new Card(ColorType.CUTE,5000,5000,5000,30, new CenterSkill(CenterSkill.Condition.ANY,ColorType.CUTE, AppealType.ANY,30), new SkillHeal(7,90,6)));
        deck.SetCard(1,new Card(ColorType.COOL,5000,5000,5000,30, new CenterSkill(CenterSkill.Condition.ANY,ColorType.CUTE, AppealType.ANY,30), new SkillPerfectSupport(8,99,8)));
        deck.SetCard(2,new Card(ColorType.COOL,5000,5000,5000,30, new CenterSkill(CenterSkill.Condition.TRICOLOR,ColorType.ANY, AppealType.ANY,40), new SkillScoreUp(4,60,3)));
        deck.SetCard(3,new Card(ColorType.PASSION,5000,5000,5000,30, new CenterSkill(CenterSkill.Condition.ANY,ColorType.CUTE, AppealType.ANY,30), new SkillBoost(4,99,4)));
        deck.SetCard(4,new Card(ColorType.PASSION,5000,5000,5000,30, new CenterSkill(CenterSkill.Condition.ANY,ColorType.CUTE, AppealType.ANY,30), new SkillDamageGuard(4,100,4)));
        deck.ApplyCenterSkill();
        btStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final NumberPicker np=new NumberPicker(ChooseDeckActivity.this);
                np.setMinValue(5);
                np.setMaxValue(40);
                LinearLayout ll=new LinearLayout(ChooseDeckActivity.this);
                Button btSt=new Button(ChooseDeckActivity.this);
                btSt.setText("Start");
                btSt.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(ChooseDeckActivity.this, GameActivity.class);
                        Difficulty difff=new Difficulty(Difficulties.valueOf(difficulty),path,100,np.getValue());
                        i.putExtra("difficulty",difff);
						i.putExtra("notefile",nf);
						i.putExtra("deck",deck);
						i.putExtra("speed",np.getValue());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                });
                ll.addView(np);
                ll.addView(btSt);
                AlertDialog ad=new AlertDialog.Builder(ChooseDeckActivity.this)
                        .setCancelable(true)
                        .setTitle("Choose speed")
                        .setView(ll)
                        .show();
            }
        });
    }
}
