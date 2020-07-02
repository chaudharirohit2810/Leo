package com.example.leo_kotlin.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.leo_kotlin.repository.TroubleRepository
import com.example.leo_kotlin.utils.getUserFromCache
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class PowerButtonReceiver : BroadcastReceiver() {
    private var isTriggerInProgress: Boolean = false
    private var lastTriggerTime : Long = 0L
    private var THREE_SECONDS: Long = 3 * 1000
    private var count: Int = 0
    private lateinit var repository: TroubleRepository
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action?.contains(Intent.ACTION_SCREEN_OFF)!!
            || intent?.action?.contains(Intent.ACTION_SCREEN_ON)!!) {
            if(!isTriggerInProgress) {
                checkAlert(context!!)
            }
            Timber.d("Power Button Pressed")
        }
    }

    private fun checkAlert(context: Context) {
        if(((System.currentTimeMillis() - lastTriggerTime) <= THREE_SECONDS || count <= 1) && count <= 2 && !isTriggerInProgress)
        {
            count++
            isTriggerInProgress = false
            lastTriggerTime = System.currentTimeMillis()
        }else {
            isTriggerInProgress = true
            Timber.d("Alert User")
            val user = getUserFromCache(context) ?: throw Exception("User not available in cache")
            GlobalScope.launch {
                try {
                    TroubleRepository.getInstance(context).markTrouble(user)
                    count = 0;
                    isTriggerInProgress = false
                }catch (e: Exception) {
                    Timber.d("Error in marking trouble")
                    count = 0
                    isTriggerInProgress = false
                }

            }
        }
    }
}