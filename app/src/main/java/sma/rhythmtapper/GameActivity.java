package sma.rhythmtapper;

import android.content.Intent;

import sma.rhythmtapper.framework.Impl.RTGame;
import sma.rhythmtapper.framework.Screen;
import sma.rhythmtapper.game.LoadingScreen;
import sma.rhythmtapper.game.NoteFile.NoteFile;
import sma.rhythmtapper.game.models.Deck;
import sma.rhythmtapper.models.Difficulty;

public class GameActivity extends RTGame {
    private Difficulty _diff;

    @Override
    public Screen getInitScreen() {
        // get passed difficulty object
        Intent intent = getIntent();
        _diff = (Difficulty) intent.getSerializableExtra("difficulty");
        setDeck((Deck) intent.getSerializableExtra("deck"));
        setNoteFile((NoteFile) intent.getSerializableExtra("notefile"));
        setBallspeed((int) intent.getIntExtra("speed", 10));
        setAutoPlay(intent.getBooleanExtra("autoplay", false));
        return new LoadingScreen(this, _diff);
    }
}
