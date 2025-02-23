package com.example.audiocalendar

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "AUDIOCALENDAR_CHANNEL",  // Csatorna ID
                "AudioCalendar háttérszolgáltatás", // Megjelenő név
                NotificationManager.IMPORTANCE_LOW // Alacsony prioritás, hogy ne zavarja a felhasználót
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }
}