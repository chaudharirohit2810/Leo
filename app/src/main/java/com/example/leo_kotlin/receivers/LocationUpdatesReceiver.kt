package com.example.leo_kotlin.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class LocationUpdatesReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d( "onReceive() context:$context, intent:$intent")
    }
}