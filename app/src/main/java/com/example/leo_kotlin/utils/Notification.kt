package com.example.leo_kotlin.utils

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.leo_kotlin.R

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