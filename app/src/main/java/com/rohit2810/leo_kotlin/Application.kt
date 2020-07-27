package com.rohit2810.leo_kotlin

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
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
            var channel2 = NotificationChannel(
                "FallDetection",
                "Full Screen Notification Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(channel)
            manager!!.createNotificationChannel(channel2)
        }
    }

    companion object{
        var shouldShowSplash : Boolean = true
        val peers = mutableListOf<WifiP2pDevice>()
        var manager : WifiP2pManager? = null
        var channel : WifiP2pManager.Channel? = null
        var peerListListener: WifiP2pManager.PeerListListener? = null
        var connectionInfoListener : WifiP2pManager.ConnectionInfoListener? = null
        var isConnected: Boolean = false

    }
}