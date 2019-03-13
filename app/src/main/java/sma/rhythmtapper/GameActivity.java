package sma.rhythmtapper;

import android.content.*;
import sma.rhythmtapper.framework.*;
import sma.rhythmtapper.framework.Impl.*;
import sma.rhythmtapper.game.*;
import sma.rhythmtapper.game.NoteFile.*;
import sma.rhythmtapper.game.models.*;
import sma.rhythmtapper.models.*;

public class GameActivity extends RTGame {
    private Difficulty _diff;
	
    @Override
    public Screen getInitScreen() {
        // get passed difficulty object
		Intent intent=getIntent();
        _diff = (Difficulty)intent.getSerializableExtra("difficulty");
		setDeck((Deck)intent.getSerializableExtra("deck"));
		setNoteFile((NoteFile)intent.getSerializableExtra("notefile"));
		setBallspeed((int)intent.getIntExtra("speed",10));
        return new LoadingScreen(this, _diff);
    }
}
