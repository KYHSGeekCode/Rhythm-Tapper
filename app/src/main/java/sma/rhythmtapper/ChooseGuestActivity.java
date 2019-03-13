package sma.rhythmtapper;

import android.app.*;
import android.content.*;
import android.os.*;
import sma.rhythmtapper.game.NoteFile.*;
import sma.rhythmtapper.game.models.*;

public class ChooseGuestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_guest);
        Intent intent = getIntent();
        String diff = intent.getStringExtra("Difficulty");
        //String name = intent.getStringExtra("Name");
		NoteFile nf=(NoteFile) intent.getSerializableExtra("notefile");
		//Deck deck=(Deck) intent.getSerializableExtra("deck");
        Intent i = new Intent(this, ChooseDeckActivity.class);
        // add flag, when activity already runs,
        // use it instead of launching a new instance
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("Difficulty",diff);
        //i.putExtra("Name",name);
		i.putExtra("notefile",nf);
		i.putExtra("guest",Card.nullCard);
		//i.putExtra("deck",deck);
        startActivity(i);
    }
}
