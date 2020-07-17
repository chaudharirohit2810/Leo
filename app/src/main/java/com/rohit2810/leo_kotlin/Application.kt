package com.rohit2810.leo_kotlin

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import timber.log.Timber

class Application: Application() {
    lateinit var channel : NotificationChannel
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = NotificationChannel(
                "ServiceChannel",
                "Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(channel)
        }
    }

    companion object{
        var shouldShowSplash : Boolean = true
    }
}