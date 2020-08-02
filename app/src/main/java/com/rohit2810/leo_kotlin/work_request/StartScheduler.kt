package com.rohit2810.leo_kotlin.work_request

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.rohit2810.leo_kotlin.services.FallDetectionService
import com.rohit2810.leo_kotlin.utils.showToast
import timber.log.Timber

class StartScheduler(var appContext: Context, params: WorkerParameters) : Worker(appContext, params) {
    override fun doWork(): Result {
        //TODO: start the service

        Timber.d("Fall Detection Started")
        var intent = Intent(appContext, FallDetectionService::class.java)
        ContextCompat.startForegroundService(appContext, intent)
        return Result.success()
    }

}