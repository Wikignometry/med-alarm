package com.medalarm.medalarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.medalarm.medalarm.util.logDebug


/* Receives signals when the alarm is reached and starts the TriggeredAlarmService */
class AlarmTriggerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        logDebug("alarm triggered")
        launchService(context)
    }
}

fun launchService(context: Context) {
    val serviceIntent = Intent(context, TriggeredAlarmService::class.java)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(serviceIntent)
    } else {
        context.startService(serviceIntent)
    }
}
