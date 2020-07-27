package com.rohit2810.leo_kotlin.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rohit2810.leo_kotlin.repository.TroubleRepository
import com.rohit2810.leo_kotlin.utils.getUserFromCache
import com.rohit2810.leo_kotlin.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class TroubleReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("Message Received")
        var coroutineScope = CoroutineScope(Dispatchers.Main)
        try {
            coroutineScope.launch {
                TroubleRepository.getInstance(context).markTrouble(getUserFromCache(context)!!)
            }
        }catch (e: Exception) {
            context.showToast("Something went wrong!!")
        }

    }

}