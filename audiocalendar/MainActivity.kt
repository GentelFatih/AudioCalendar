package com.example.audiocalendar

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.audiocalendar.service.MyForegroundService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Indítja a háttérszolgáltatást az Android verziótól függően
        val intent = Intent(this, MyForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent) // Android 8.0+ (API 26+)
        } else {
            startService(intent) // Android 7.1- (API 25-)
        }

        // Befejezi az Activity-t, hogy ne maradjon vizuális felület
        finish()
    }
}