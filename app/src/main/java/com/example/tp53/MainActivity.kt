package com.example.musicapp

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MusicService : Service() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.getAction() != null) {
            val action = intent.getAction()

            if (action == PLAY) {
                playMusic()
            } else if (action == PAUSE) {
                pauseMusic()
            } else if (action == STOP) {
                stopMusic()
                stopForeground(true)
                stopSelf()
                return START_NOT_STICKY
            }
        }

        return START_STICKY
    }

    private fun playMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.music.mp3)
        }

        if (mediaPlayer != null && !mediaPlayer!!.isPlaying()) {
            mediaPlayer!!.start()
        }

        showNotification("Lecture")
    }

    private fun pauseMusic() {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying()) {
            mediaPlayer!!.pause()
        }

        showNotification("En Pause")
    }

    private fun stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    CHANNEL_ID,
                    "Music Controls",
                    NotificationManager.IMPORTANCE_LOW
                )

            val manager =
                getSystemService<NotificationManager?>(NotificationManager::class.java)

            manager.createNotificationChannel(channel)
        }
    }

    private fun getActionIntent(action: String?, requestCode: Int): PendingIntent? {
        val intent = Intent(this, MusicService::class.java)
        intent.setAction(action)

        return PendingIntent.getService(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun showNotification(status: String?) {
        createNotificationChannel()

        val openIntent: Intent = Intent(this, MainActivity::class.java)

        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val notification: Notification =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Music Player")
                .setContentText(status)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .addAction(
                    0, "Lecture",
                    getActionIntent(PLAY, 1)
                )
                .addAction(
                    0, "Pause",
                    getActionIntent(PAUSE, 2)
                )
                .addAction(
                    0, "Arrêter",
                    getActionIntent(STOP, 3)
                )
                .build()

        startForeground(1, notification)
    }

    override fun onDestroy() {
        stopMusic()

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val PLAY: String = "com.example.PLAY"
        const val PAUSE: String = "com.example.PAUSE"
        const val STOP: String = "com.example.STOP"

        private const val CHANNEL_ID = "music_channel"
    }
}