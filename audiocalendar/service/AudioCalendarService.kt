package com.example.audiocalendar.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.audiocalendar.R

class MyForegroundService : Service() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()  // Értesítési csatorna létrehozása
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(1, createNotification())
        // FONTOS: ezt azonnal meg kell hívni!

        return START_STICKY
    }

    private fun createNotification(): Notification {
        val channelId = "audio_calendar_channel"
        val notificationManager = getSystemService(NotificationManager::class.java)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("AudioCalendar Fut")
            .setContentText("A háttérben figyel a hangparancsokra")
            .setSmallIcon(R.drawable.ic_notification)  // Győződj meg róla, hogy ez az ikon létezik!
            .setPriority(NotificationCompat.PRIORITY_LOW)

        return notificationBuilder.build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "audio_calendar_channel",
                "AudioCalendar Szolgáltatás",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}