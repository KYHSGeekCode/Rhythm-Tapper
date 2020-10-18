package sma.rhythmtapper

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {
    val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            }

    val sdcardFile = Environment.getExternalStorageDirectory()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_btn_start.setOnClickListener {
            var text = etPath.text.toString()
            if (text.isEmpty()) text = File(sdcardFile, "Android/data/com.nomansland.tempestwave/files/").canonicalPath
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
        when {
            ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                        Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                startActivity(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
            }
        } else {
//            TODO("VERSION.SDK_INT < R")

        }
//        requestPermission
    }
}