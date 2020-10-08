package sma.rhythmtapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import sma.rhythmtapper.game.NoteFile.NoteFile;
import sma.rhythmtapper.game.models.AppealType;
import sma.rhythmtapper.game.models.Card;
import sma.rhythmtapper.game.models.CenterSkill;
import sma.rhythmtapper.game.models.ColorType;
import sma.rhythmtapper.game.models.Deck;
import sma.rhythmtapper.game.models.Difficulties;
import sma.rhythmtapper.game.models.Skill.SkillBoost;
import sma.rhythmtapper.game.models.Skill.SkillDamageGuard;
import sma.rhythmtapper.game.models.Skill.SkillHeal;
import sma.rhythmtapper.game.models.Skill.SkillScoreUp;
import sma.rhythmtapper.models.Difficulty;

public class ChooseDeckActivity extends Activity {
    Button btStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_deck);
        btStart = (Button) findViewById(R.id.chooseDeckBtStart);
        Intent intent = getIntent();
        final NoteFile nf = (NoteFile) intent.getSerializableExtra("notefile");
        final String path = nf.getMusicPath(); //intent.getStringExtra("Name");
        final String videoPath = nf.getVideoPath();
        final String difficulty = intent.getStringExtra("Difficulty");
        final Deck deck = new Deck();
        deck.SetCard(0, new Card(ColorType.CUTE, 5000, 5000, 5000, 30, new CenterSkill(CenterSkill.Condition.ANY, ColorType.CUTE, AppealType.ANY, 30), new SkillHeal(7, 100, 7)));
        deck.SetCard(1, new Card(ColorType.COOL, 5000, 5000, 5000, 30, new CenterSkill(CenterSkill.Condition.ANY, ColorType.CUTE, AppealType.ANY, 30), new SkillHeal(8, 100, 8)));
        deck.SetCard(2, new Card(ColorType.COOL, 5000, 5000, 5000, 30, new CenterSkill(CenterSkill.Condition.TRICOLOR, ColorType.ANY, AppealType.ANY, 40), new SkillScoreUp(4, 80, 3)));
        deck.SetCard(3, new Card(ColorType.PASSION, 5000, 5000, 5000, 30, new CenterSkill(CenterSkill.Condition.ANY, ColorType.CUTE, AppealType.ANY, 30), new SkillBoost(4, 1, 4)));
        deck.SetCard(4, new Card(ColorType.PASSION, 5000, 5000, 5000, 30, new CenterSkill(CenterSkill.Condition.ANY, ColorType.CUTE, AppealType.ANY, 30), new SkillDamageGuard(4, 100, 4)));
        deck.ApplyCenterSkill();
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NumberPicker np = new NumberPicker(ChooseDeckActivity.this);
                final CheckBox cb = new CheckBox(ChooseDeckActivity.this);
                np.setMinValue(5);
                np.setMaxValue(40);
                cb.setChecked(false);
                LinearLayout ll = new LinearLayout(ChooseDeckActivity.this);
                Button btSt = new Button(ChooseDeckActivity.this);
                btSt.setText("Start");
                btSt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(ChooseDeckActivity.this, GameActivity.class);
                        Difficulty difff = new Difficulty(Difficulties.valueOf(difficulty), path, videoPath, 100, np.getValue());
                        i.putExtra("difficulty", difff);
                        i.putExtra("notefile", nf);
                        i.putExtra("deck", deck);
                        i.putExtra("speed", np.getValue());
                        i.putExtra("autoplay", cb.isChecked());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                });
                ll.addView(np);
                ll.addView(cb);
                ll.addView(btSt);
                AlertDialog ad = new AlertDialog.Builder(ChooseDeckActivity.this)
                        .setCancelable(true)
                        .setTitle("Choose speed")
                        .setView(ll)
                        .show();
            }
        });
    }
}
