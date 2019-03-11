package sma.rhythmtapper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import sma.rhythmtapper.framework.Game;
import sma.rhythmtapper.game.NoteFile.*;

public class ChooseGuestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_guest);
        Intent intent = getIntent();
        String diff = intent.getStringExtra("Difficulty");
        //String name = intent.getStringExtra("Name");
		NoteFile nf=(NoteFile) intent.getSerializableExtra("notefile");
        Intent i = new Intent(this, ChooseDeckActivity.class);
        // add flag, when activity already runs,
        // use it instead of launching a new instance
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("Difficulty",diff);
        //i.putExtra("Name",name);
		i.putExtra("notefile",nf);
        startActivity(i);
    }
}
