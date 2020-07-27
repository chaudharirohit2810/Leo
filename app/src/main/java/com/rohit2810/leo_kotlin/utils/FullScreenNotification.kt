package com.rohit2810.leo_kotlin.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.rohit2810.leo_kotlin.MainActivity
import com.rohit2810.leo_kotlin.R
import com.rohit2810.leo_kotlin.receivers.NotificationReceiver

fun Context.showNotificationWithFullScreenIntent(
    isLockScreen: Boolean = false,
    channelId: String = CHANNEL_ID,
    title: String = "Trouble",
    description: String = "You've marked yourself in trouble"
) {

    val broadcastIntent = Intent(this, NotificationReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, 0)

    val builder = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(android.R.drawable.arrow_up_float)
        .setColor(Color.RED)
        .setContentTitle(title)
        .setContentText(description)
        .addAction(R.drawable.leo2, "I am not in trouble", pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setFullScreenIntent(getFullScreenIntent(isLockScreen), true)

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    with(notificationManager) {
        buildChannel()
        val notification = builder.build()
        notify(0, notification)
    }
}

private fun NotificationManager.buildChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Example Notification Channel"
        val descriptionText = "This is used to demonstrate the Full Screen Intent"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        createNotificationChannel(channel)
    }
}

private fun Context.getFullScreenIntent(isLockScreen: Boolean): PendingIntent {
    val destination = if (isLockScreen)
        MainActivity::class.java
    else
        MainActivity::class.java
    val intent = Intent(this, destination)

    // flags and request code are 0 for the purpose of demonstration
    return PendingIntent.getActivity(this, 0, intent, 0)
}

private const val CHANNEL_ID = "channelId"