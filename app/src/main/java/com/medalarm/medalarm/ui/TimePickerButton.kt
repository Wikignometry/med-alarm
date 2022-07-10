package com.medalarm.medalarm.ui

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.medalarm.medalarm.showTimePicker

// from https://github.com/Kiran-Bahalaskar/Time-Picker-With-Jetpack-Compose/blob/master/app/src/main/java/com/kiranbahalaskar/timepicker/MainActivity.kt
@Composable
fun TimePickerButton(onValueChange: (Long) -> Unit,
                     content: @Composable() () -> Unit) {
    val context = LocalContext.current
    Button(onClick = {
        showTimePicker(context, onValueChange)
    }) {
        content()
    }
}