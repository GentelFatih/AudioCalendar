package com.example.audiocalendar.service

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private lateinit var ttsService: TextToSpeechService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize TextToSpeechService
        ttsService = TextToSpeechService(this)

        // Start the foreground service based on Android version
        val intent = Intent(this, MyForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }

        // Speak startup message
        ttsService.speakText("AudioCalendar elindult") {
            // After the first speech, start the test speech after a short delay
            Handler(Looper.getMainLooper()).postDelayed({
                ttsService.speakText("Ez egy teszt.") {
                    finish() // Finish after both messages are spoken
                }
            }, 1000) // 1 second delay
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsService.shutdown()
    }
}