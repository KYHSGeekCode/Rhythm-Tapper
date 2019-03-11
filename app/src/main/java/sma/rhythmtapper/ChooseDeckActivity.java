package sma.rhythmtapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.io.Serializable;

import sma.rhythmtapper.game.models.Difficulties;
import sma.rhythmtapper.models.Difficulty;

public class ChooseDeckActivity extends Activity {
    Button btStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_deck);
        btStart= (Button) findViewById(R.id.chooseDeckBtStart);
        Intent intent=getIntent();
        final String path = intent.getStringExtra("Name");
        final String difficulty = intent.getStringExtra("Difficulty");
        btStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final NumberPicker np=new NumberPicker(ChooseDeckActivity.this);
                np.setMinValue(10);
                np.setMaxValue(100);
                LinearLayout ll=new LinearLayout(ChooseDeckActivity.this);
                Button btSt=new Button(ChooseDeckActivity.this);
                btSt.setText("Start");
                btSt.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(ChooseDeckActivity.this, GameActivity.class);
                        Difficulty difff=new Difficulty(Difficulties.valueOf(difficulty),path,100,np.getValue());
                        i.putExtra("difficulty",difff);
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
