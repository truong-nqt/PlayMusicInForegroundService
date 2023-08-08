package com.example.foregroundserviceandnotification

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            R.drawable.ic_launcher_foreground,
            R.raw.atmyworst)
        val intent = Intent(this, MyService::class.java)
        val json = Gson().toJson(song)

        intent.putExtra("key_object_song", json)
        startService(intent)
    }

    private fun stopService() {
        val intent = Intent(this, MyService::class.java)
        stopService(intent)
    }
}