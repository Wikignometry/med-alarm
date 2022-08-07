package com.medalarm.medalarm.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.medalarm.medalarm.AlarmTriggerReceiver
import java.sql.Time
import java.util.*

fun scheduleAlarms(context: Context, alarmManager: AlarmManager, start: Time, end: Time, count: Int) {
    val tag = "medalarm"

    val activity = context.getActivity()
    if (activity == null) {
        Log.e(tag, "Activity was null!")
        return
    }

    val convCal = Calendar.getInstance()

    convCal.time = start
    val startTime = convCal.timeInMillis

    convCal.time = end
    val endTime = convCal.timeInMillis

    val delta = endTime - startTime
    val gap = delta / count

    // end inclusive
    for (i in 1..count) {
        scheduleAlarm(context, alarmManager, startTime + gap * i, i)
    }
}

fun scheduleAlarm(context: Context, alarmManager: AlarmManager, triggerAtMillis: Long, id: Int) {
    val intent = Intent(context, AlarmTriggerReceiver::class.java)
    intent.action = "PILL_ALARM_ACTION"

    val pendingIntent = PendingIntent.getBroadcast(context, id, intent,
        PendingIntent.FLAG_IMMUTABLE
    )

    Log.d("medalarm", "setting an alarm")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    } else {
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }

    Toast.makeText(context, "Scheduled alarm ${id}.", Toast.LENGTH_LONG).show() // temporary?
}

fun setAlarms(context: Context, start: Time, end: Time, amt: Int) {
    val tag = "medalarm"

    val activity = context.getActivity()
    if (activity == null) {
        Log.e(tag, "Activity was null!")
        return
    }

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        when {
            alarmManager.canScheduleExactAlarms() -> {
                scheduleAlarms(context, alarmManager, start, end, amt)
            }
            else -> {
                // go to exact alarm settings
                Intent().apply {
                    action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                }.also {
                    ContextCompat.startActivity(context, it, null)
                }
            }
        }
    } else {
        scheduleAlarms(context, alarmManager, start, end, amt)
    }
}