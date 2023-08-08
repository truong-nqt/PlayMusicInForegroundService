package com.example.foregroundserviceandnotification

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.gson.Gson

class MyService: Service() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val json = intent.getStringExtra("key_object_song")
            val song = Gson().fromJson(json, Song::class.java)
            if (song != null) {
                startMusic(song)
                senNotification(song)
            }
        }
        return START_NOT_STICKY
    }

    private fun startMusic(song: Song) {
        mediaPlayer = MediaPlayer.create(applicationContext, song.resource)
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun senNotification(song: Song) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val remoteViews = RemoteViews(packageName, R.layout.layout_notification)
        remoteViews.setTextViewText(R.id.text_title_song, song.title)
        remoteViews.setTextViewText(R.id.text_singer_song, song.singer)
        remoteViews.setImageViewBitmap(R.id.image_song,
            BitmapFactory.decodeResource(resources, song.image))

        val notification: Notification = NotificationCompat.Builder(this, MyApplication.CHANEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setCustomContentView(remoteViews)
            .setSound(null)
            .build()

        startForeground(1, notification)
    }
}