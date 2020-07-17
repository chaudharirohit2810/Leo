package com.rohit2810.leo_kotlin.utils

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.rohit2810.leo_kotlin.R
import com.rohit2810.leo_kotlin.receivers.NotificationReceiver
import java.util.concurrent.TimeUnit

private val serviceChannel = "ServiceChannel"

fun foregroundNotification(context: Context, pendingIntent: PendingIntent?) : Notification {
    val notification = NotificationCompat.Builder(context, serviceChannel)
        .setContentTitle("Leo Platform")
        .setSmallIcon(R.drawable.leo2)
        .setContentText("Leo is ready to help")
        .setContentIntent(pendingIntent)
        .build();
    return notification
}

@RequiresApi(Build.VERSION_CODES.KITKAT)
fun Context.scheduleNotification(isLockScreen: Boolean) {
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val timeInMillis = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(SCHEDULE_TIME)

    with(alarmManager) {
        setExact(AlarmManager.RTC_WAKEUP, timeInMillis, getReceiver(isLockScreen))
    }
}

private fun Context.getReceiver(isLockScreen: Boolean): PendingIntent {
    // for demo purposes no request code and no flags
    return PendingIntent.getBroadcast(
        this,
        0,
        NotificationReceiver.build(this, isLockScreen),
        0
    )
}

private const val SCHEDULE_TIME = 5L