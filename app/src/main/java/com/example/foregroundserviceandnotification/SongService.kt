package com.example.foregroundserviceandnotification

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.gson.Gson

class SongService: Service() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            Actions.START.toString() -> {
                val json = intent.getStringExtra(Constants.KEY_OBJECT_SONG)
                val song = Gson().fromJson(json, Song::class.java)
                startMusic(song)
                senNotification(song)
            }
            Actions.STOP.toString() -> {
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun startMusic(song: Song) {
        mediaPlayer = MediaPlayer.create(applicationContext, song.resource)
        mediaPlayer.start()
    }

    private fun senNotification(song: Song) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val remoteViews = RemoteViews(packageName, R.layout.layout_notification)
        remoteViews.setTextViewText(R.id.text_title_song, song.title)
        remoteViews.setTextViewText(R.id.text_singer_song, song.singer)
        remoteViews.setImageViewResource(R.id.image_song, song.image)
        val notification: Notification = NotificationCompat.Builder(this, RunningApp.CHANEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setCustomContentView(remoteViews)
            .setSound(null)
            .build()
        startForeground(1, notification)
    }

    enum class Actions{
        START, STOP
    }
}