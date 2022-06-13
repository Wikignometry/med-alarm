package com.medalarm.medalarm

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.medalarm.medalarm.ui.theme.MedAlarmTheme
import java.security.AccessController.getContext
import java.util.*



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var time = ""
        setContent {
            Column(

                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            )
            {
                Log.d("test", "message check")
                time = NowButton("I just took my pill", "Time logged!", time)
//                time = TimePickerButton("I already took my pill", "Time Logged!", time)
                Text(text = "I took my first pill at: $time")
//                SimpleOutlinedTextFieldSample()

            }
        }
    }
}

// from https://github.com/Kiran-Bahalaskar/Time-Picker-With-Jetpack-Compose/blob/master/app/src/main/java/com/kiranbahalaskar/timepicker/MainActivity.kt
@Composable
fun TimePickerButton(label: String, text: String, fixedTime: String): String {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]
    var time = fixedTime
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour: Int, minute: Int ->
            time = "$hour:$minute"
        }, hour, minute, false
    )
        Button(onClick = {
            timePickerDialog.show()
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(context, text, duration)
            toast.show()
        }) {
            Text(text = label)
        }
    return time
}

// from https://developer.android.com/jetpack/compose/text
@Composable
fun SimpleOutlinedTextFieldSample() {
    Text(text = "I will take my pill")
    var text by remember { mutableStateOf("") }
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Label") }
    )
    Text(text = "more times today")
}

@Composable
fun NowButton(label: String, text: String, fixedTime: String): String {
    Log.d("test", "NowButton")
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]
    var time = fixedTime
    Button(onClick = {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(context, text, duration)
        toast.show()
        time = "$hour:$minute"
        Log.d("test", "$hour:$minute")
        Log.d("test", "$time")
    })
    {
    Text(label)
    }
    Log.d("test", "$time")
    return time
}