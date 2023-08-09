package com.example.foregroundserviceandnotification

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        val buttonStartService = findViewById<Button>(R.id.btn_start_service)
        val buttonStopService = findViewById<Button>(R.id.btn_stop_service)
        buttonStartService.setOnClickListener {
            startService()
        }
        buttonStopService.setOnClickListener {
            stopService()
        }
    }

    private fun startService() {
        val song = Song(
            "Big city boy",
            "Boy",
            R.drawable.ic_launcher_background,
            R.raw.atmyworst)
        val json = Gson().toJson(song)
        Intent(this, SongService::class.java).also {
            it.action = SongService.Actions.START.toString()
            it.putExtra(Constants.KEY_OBJECT_SONG, json)
            startService(it)
        }
    }

    private fun stopService() {
        Intent(this, SongService::class.java).also {
            it.action = SongService.Actions.STOP.toString()
            startService(it)
        }
    }
}