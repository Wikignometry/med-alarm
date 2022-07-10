package com.medalarm.medalarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/*
 * Receives signals sent by the alarm notification's actions (dismiss, possibly snooze)
 */
class AlarmActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // cancel notification with passed in notif_id
        val notif_id = intent.getIntExtra("notif_id", -1)
        if (notif_id > 0) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notif_id)
        }

        Log.d("medalarm", "cancelled notif")
    }
}