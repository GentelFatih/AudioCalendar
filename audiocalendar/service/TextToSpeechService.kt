package com.example.audiocalendar.service

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale

class TextToSpeechService(context: Context) {
    private var tts: TextToSpeech? = null
    private var onSpeechCompleted: (() -> Unit)? = null
    private var isTtsReady = false

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val locale = Locale("hu", "HU")
                val available = tts?.isLanguageAvailable(locale)
                println("DEBUG: Language availability check result: $available")

                if (available == TextToSpeech.LANG_AVAILABLE || available == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                    tts?.language = locale

                    val voices = tts?.voices
                    println("DEBUG: Available voices: $voices")

                    val hungarianVoice = voices?.find { it.locale.language == "hu" && !it.features.contains("notInstalled") }
                    if (hungarianVoice != null) {
                        tts?.voice = hungarianVoice
                        println("DEBUG: Selected Hungarian voice: ${hungarianVoice.name}")
                    } else {
                        println("TTS WARNING: No specific Hungarian voice found, using default.")
                    }

                    isTtsReady = true
                    println("DEBUG: TTS készen áll! isTtsReady = $isTtsReady")  // <-- EZT ADD HOZZÁ!

                    setupUtteranceListener()

                    speakText("Teszt üzenet, működik a szövegfelolvasás?")

                } else {
                    println("TTS ERROR: Hungarian language is not available or not supported!")
                }
            } else {
                println("TTS ERROR: Initialization failed!")
            }
        }
    }

    private fun setupUtteranceListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                println("TTS: Speech started")
            }

            override fun onDone(utteranceId: String?) {
                println("TTS: Speech completed")
                onSpeechCompleted?.invoke()
            }

            override fun onError(utteranceId: String?) {
                println("TTS ERROR: Speech failed")
            }
        })
    }

    fun speakText(text: String, onComplete: (() -> Unit)? = null) {
        if (!isTtsReady) {
            println("TTS ERROR: TTS is not initialized or language is unsupported!")
            return
        }

        onSpeechCompleted = onComplete
        println("DEBUG: Attempting to speak: $text")

        val params = Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "TTS_ID")

        val result = tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "TTS_ID")

        if (result == TextToSpeech.ERROR) {
            println("TTS ERROR: speak() failed! Lehetséges okok:")
            println("- A TTS motor nincs beállítva")
            println("- A magyar nyelvi adat nincs telepítve")
            println("- A készülék TTS szolgáltatása hibás")
        } else {
            println("DEBUG: speak() sikeresen meghívva, result: $result")
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null // Megakadályozza a memória-szivárgást
    }
}