package com.rohit2810.leo_kotlin.utils

import android.content.Context
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pManager
import android.os.AsyncTask
import com.rohit2810.leo_kotlin.Application
import com.rohit2810.leo_kotlin.Application.Companion.channel
import com.rohit2810.leo_kotlin.Application.Companion.connectionInfoListener
import com.rohit2810.leo_kotlin.Application.Companion.isConnected
import com.rohit2810.leo_kotlin.Application.Companion.manager
import com.rohit2810.leo_kotlin.models.Trouble
import timber.log.Timber
import java.net.ServerSocket

fun Context.connectP2P() {
    Timber.d("Trying to connect")
    val context = this
    if (!Application.peers.isEmpty()) {
        Timber.d("Connecting to peers")
        val config = WifiP2pConfig()
        config.deviceAddress = Application.peers.get(0).deviceAddress
        channel?.also {
            manager?.connect(it, config, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    context.showToast("Device Connection Successful")
                    manager?.requestConnectionInfo(channel, connectionInfoListener)
                }
                override fun onFailure(reason: Int) {
                    context.showToast("Failed to connect: ${reason}")
                }
            })
        }
    } else {
        context.showToast("Connection Failed")
    }
}

fun stringToTrouble(msg: String): Trouble?{
    var list = msg.lines()
    var contact = mutableListOf<String?>()
    var i = 0
    for (item in list) {
        if (i < 4) {
            i++
        }else {
            contact.add(item)
        }
    }
    var trouble : Trouble? = null
    if (!list.isEmpty()) {
        trouble = Trouble(
            username = list[0] ?: " ",
            inTrouble = true,
            latitude = list[1].toDouble() ?: 0.0,
            longitude = list[2].toDouble() ?: 0.0,
            token = list[3],
            emergencyContacts = contact
        )
    }
    return trouble
}