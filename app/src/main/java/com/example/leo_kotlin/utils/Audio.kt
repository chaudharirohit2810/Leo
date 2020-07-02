package com.example.leo_kotlin.utils

import android.media.AudioManager
import android.media.ToneGenerator

fun playAudio() {
    val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 100)
    toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 2000)
}