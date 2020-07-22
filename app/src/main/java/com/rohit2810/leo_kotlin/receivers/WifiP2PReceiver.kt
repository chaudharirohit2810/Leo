package com.rohit2810.leo_kotlin.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pManager
import com.rohit2810.leo_kotlin.Application
import timber.log.Timber

class WifiP2PReceiver(
    val manager: WifiP2pManager,
    val listener: WifiP2pManager.PeerListListener,
    val channel: WifiP2pManager.Channel,
    var connectionInfoListener: WifiP2pManager.ConnectionInfoListener
) :
    BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                when (state) {
                    WifiP2pManager.WIFI_P2P_STATE_ENABLED -> {
                        Timber.d("Wifi is enabled")
                        manager.requestPeers(channel, listener)
                    }
                    else -> {
                        Timber.d("Wifi is disabled")
                    }
                }
            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                Timber.d("Requesting peers")
                manager.requestPeers(channel, listener)
            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                Timber.d("Connection Change")
                var networkInfo = intent.getParcelableExtra<NetworkInfo>(WifiP2pManager.EXTRA_NETWORK_INFO)
                if (networkInfo.isConnected()) {
                    Application.isConnected = true
                    Timber.d("Device connected")
                    manager.requestConnectionInfo(channel, connectionInfoListener)
                }else {
                    Application.isConnected = false
                    Timber.d("Device Disconnected")
                    manager.requestPeers(channel, listener)
                }
            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                // When the wifi is turned off or on
                Timber.d("Inside Device changed action")

            }
        }
    }

}