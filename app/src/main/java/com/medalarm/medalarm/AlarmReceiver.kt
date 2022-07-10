package com.medalarm.medalarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        launchNotification(context)
    }
}

fun launchNotification(context: Context) {
    val NOTIF_ID = 1
    val CHANNEL_ID = "ALARM"

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(CHANNEL_ID, "Alarm", importance)
        mChannel.description = "placeholder"
        mChannel.enableLights(true)
        mChannel.lightColor = Color.RED
        mChannel.enableVibration(true)
        mChannel.vibrationPattern =
            longArrayOf(50, 200, 400)
        mChannel.setShowBadge(true)
        notificationManager.createNotificationChannel(mChannel)
    }

    val alarmIntent = Intent(context, AlarmActivity::class.java)
    alarmIntent.action = "PILL_ALARM_ACTION"

    val alarmPendingIntent = PendingIntent.getActivity(
        context,
        0,
        alarmIntent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val alarmDismissIntent = Intent(context, AlarmActionReceiver::class.java)
    alarmDismissIntent.putExtra("notif_id", NOTIF_ID)

    val alarmDismissPendingIntent = PendingIntent.getBroadcast(context, -1, alarmDismissIntent, PendingIntent.FLAG_IMMUTABLE)

    Log.d("medalarm", "test!!!")
    val notif = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_alarm)
        .setContentTitle("Pill Alarm!")
        .setContentText("It's time to take a pill!")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(alarmPendingIntent)
        .setFullScreenIntent(alarmPendingIntent, true)
        .setCategory(NotificationCompat.CATEGORY_ALARM)
        .setOngoing(true)
        .addAction(R.drawable.ic_alarm_off, "Dismiss", alarmDismissPendingIntent)
        .build()

    notificationManager.notify(NOTIF_ID, notif)
}