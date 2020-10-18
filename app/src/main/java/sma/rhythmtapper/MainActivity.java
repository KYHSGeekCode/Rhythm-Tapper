package sma.rhythmtapper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import androidx.activity.result.

public class MainActivity extends Activity {

    private Button _startBtn;
    private Button _highscoreBtn;
    private Button _aboutBtn;
    private EditText _pathEdit;

//    private ActivityResultLauncher<String> requestPermissionLauncher =
//            registerForActivityResult(new RequestPermission(), isGranted -> {
//                if (isGranted) {
//                    // Permission is granted. Continue the action or workflow in your
//                    // app.
//                } else {
//                    // Explain to the user that the feature is unavailable because the
//                    // features requires a permission that the user has denied. At the
//                    // same time, respect the user's decision. Don't link to system
//                    // settings in an effort to convince the user to change their
//                    // decision.
//                }
//            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this._startBtn = (Button) this.findViewById(R.id.main_btn_start);
        this._highscoreBtn = (Button) this.findViewById(R.id.main_btn_highscore);
        this._aboutBtn = (Button) this.findViewById(R.id.main_btn_about);
        this._pathEdit = this.findViewById(R.id.etPath);

        this._startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = _pathEdit.getText().toString();
                if(text.isEmpty())
                    text= "/sdcard/Android/data/com.nomansland.tempestwave/files/";
                Intent i = new Intent(MainActivity.this, DifficultySelectionActivity.class);
                i.putExtra("path", text);
                MainActivity.this.startActivity(i);
            }
        });

        this._highscoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HighscoreActivity.class);
                MainActivity.this.startActivity(i);
            }
        });
        this._aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AboutActivity.class);
                MainActivity.this.startActivity(i);
            }
        });

//        requestPermission
    }
}
