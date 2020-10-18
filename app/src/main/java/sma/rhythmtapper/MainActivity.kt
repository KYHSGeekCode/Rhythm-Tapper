package sma.rhythmtapper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_btn_start.setOnClickListener {
            var text = etPath.text.toString()
            if (text.isEmpty()) text = "/sdcard/Android/data/com.nomansland.tempestwave/files/"
            val i = Intent(this@MainActivity, DifficultySelectionActivity::class.java)
            i.putExtra("path", text)
            this@MainActivity.startActivity(i)
        }
        main_btn_highscore.setOnClickListener {
            val i = Intent(this@MainActivity, HighscoreActivity::class.java)
            this@MainActivity.startActivity(i)
        }
        main_btn_about.setOnClickListener {
            val i = Intent(this@MainActivity, AboutActivity::class.java)
            this@MainActivity.startActivity(i)
        }

//        requestPermission
    }
}