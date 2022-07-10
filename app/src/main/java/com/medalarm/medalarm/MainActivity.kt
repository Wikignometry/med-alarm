package com.medalarm.medalarm

import android.app.*
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.medalarm.medalarm.ui.TimerScreen
import com.medalarm.medalarm.ui.theme.MedAlarmTheme
import com.medalarm.medalarm.ui.theme.isLight
import java.sql.Time
import java.util.*


class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedAlarmTheme {
                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(
                    color = MaterialTheme.colorScheme.background,
                    darkIcons = MaterialTheme.colorScheme.isLight()
                )

                TimerScreen()
            }
        }
    }
}

// Extension functions are wonderful! https://stackoverflow.com/a/68423182
tailrec fun Context.getActivity(): FragmentActivity? = when (this) {
    is FragmentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun showTimePicker(context: Context, onValueChange: (Long) -> Unit) {
    val tag = "medalarm"

    val activity = context.getActivity()
    if (activity == null) {
        Log.e(tag, "Activity was null!")
        return
    }


    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]


    val picker =
        MaterialTimePicker.Builder()
            .setTimeFormat(if (DateFormat.is24HourFormat(context)) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H)
            .setHour(hour)
            .setMinute(minute)
            .build()

    picker.addOnPositiveButtonClickListener { _ ->
        val hr = picker.hour
        val min = picker.minute
        val convCal = Calendar.getInstance()
        convCal.set(Calendar.HOUR_OF_DAY, hr)
        convCal.set(Calendar.MINUTE, min)
        convCal.set(Calendar.SECOND, 0)
        convCal.set(Calendar.MILLISECOND, 0)

        onValueChange(convCal.timeInMillis)
    }

    picker.show(activity.supportFragmentManager, tag)
}

private fun scheduleAlarms(context: Context, alarmManager: AlarmManager, start: Time, end: Time, amt: Int) {
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
    val gap = delta / amt

    // end inclusive
    for (i in 1..amt) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.action = "PILL_ALARM_ACTION"

        val pendingIntent = PendingIntent.getBroadcast(context, i, intent, FLAG_IMMUTABLE)

        Log.d("medalarm", "setting an alarm")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                RTC_WAKEUP,
                startTime + gap * i,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                RTC_WAKEUP,
                startTime + gap * i,
                pendingIntent
            )
        }
    }
    Toast.makeText(context, "Scheduled ${amt} alarms successfully.", Toast.LENGTH_LONG).show() // temporary?
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
                    action = ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                }.also {
                    startActivity(context, it, null)
                }
            }
        }
    } else {
        scheduleAlarms(context, alarmManager, start, end, amt)
    }
}