package com.rohit2810.leo_kotlin

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.rohit2810.leo_kotlin.services.LocationService
import com.rohit2810.leo_kotlin.utils.turnScreenOffAndKeyguardOn
import com.rohit2810.leo_kotlin.utils.turnScreenOnAndKeyguardOff
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }
//     resValue "string", "google_maps_key", (secretsProperties["GOOGLE_MAPS_API_KEY"] ?: "AIzaSyBdEF3JTk4k6m0MlfFyYlwlaR3Jwv1EZPc")


    override fun onDestroy() {
        super.onDestroy()
    }

}