package com.example.leo_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.leo_kotlin.services.LocationService
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        startService()
    }
//     resValue "string", "google_maps_key", (secretsProperties["GOOGLE_MAPS_API_KEY"] ?: "AIzaSyBdEF3JTk4k6m0MlfFyYlwlaR3Jwv1EZPc")

    private fun startService() {
        Timber.d("Start service")
        val intent = Intent(this, LocationService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }
}