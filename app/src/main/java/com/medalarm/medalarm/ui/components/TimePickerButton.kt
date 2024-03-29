package com.medalarm.medalarm.ui.components

import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.medalarm.medalarm.util.getActivity
import java.sql.Time
import java.util.*

// from https://github.com/Kiran-Bahalaskar/Time-Picker-With-Jetpack-Compose/blob/master/app/src/main/java/com/kiranbahalaskar/timepicker/MainActivity.kt
@Composable
fun TimePickerButton(onValueChange: (Long) -> Unit,
                     initialTime: Time,
                     content: @Composable() () -> Unit) {
    val context = LocalContext.current
    Button(onClick = {
        showTimePicker(context, initialTime, onValueChange)
    }) {
        content()
    }
}

fun showTimePicker(context: Context, initialTime: Time, onValueChange: (Long) -> Unit) {
    val tag = "medalarm"

    val activity = context.getActivity()
    if (activity == null) {
        Log.e(tag, "Activity was null!")
        return
    }

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = initialTime.time
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
