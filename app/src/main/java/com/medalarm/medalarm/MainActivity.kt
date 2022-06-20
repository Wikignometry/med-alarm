package com.medalarm.medalarm

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            )
            {
                TimerScreen()
            }
        }
    }
}

@Composable
@Preview
fun TimerScreen() {
    val context = LocalContext.current
    var sdf = SimpleDateFormat("h::mm aa")
    if (DateFormat.is24HourFormat(context)) {
        sdf = SimpleDateFormat("H::mm")
    }
    val currMs = System.currentTimeMillis()
    var time: Time by rememberSaveable { mutableStateOf(Time(currMs)) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(32.dp)
    ){
        TimePickerButton ({ ms ->
            time = Time(ms)
        }) {
            Text("I just took my pill")
        }

        TimePickerButton ({ ms ->
            time = Time(ms)
        }) {
            Text("My last pill will be taken at")
        }

        Text("I first took my pill at ${sdf.format(time)}")
//        ShowTimePicker("I already took my pill")
//        Text("next one")
//        ShowTimePicker("Last pill taken at")

        Divider(modifier = Modifier.padding(16.dp))

        Text("I will take my pill 3 more times today", fontSize = 24.sp)

        Divider(modifier = Modifier.padding(16.dp))

        Text("My last pill will be taken at ", fontSize = 24.sp)

        Button({}) {
            Text("1:30 PM")
        }
    }
}


// from https://github.com/Kiran-Bahalaskar/Time-Picker-With-Jetpack-Compose/blob/master/app/src/main/java/com/kiranbahalaskar/timepicker/MainActivity.kt
@Composable
fun TimePickerButton(onValueChange: (Long) -> Unit,
                     content: @Composable() () -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]
    val timePickerDialog = TimePickerDialog(
        context,
        {
            _, hr, min ->
            val convCal = Calendar.getInstance()
            convCal.set(Calendar.HOUR_OF_DAY, hr)
            convCal.set(Calendar.MINUTE, min)
            convCal.set(Calendar.SECOND, 0)
            convCal.set(Calendar.MILLISECOND, 0)

            onValueChange(convCal.timeInMillis)
        },
        hour, minute, DateFormat.is24HourFormat(context)
    )
    Button(onClick = {
        timePickerDialog.show()
    }) {
        content()
    }
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

//class StoreUserEmail(private val context: Context) {
//
//    // to make sure there's only one instance
//    companion object {
//        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userEmail")
//        val USER_EMAIL_KEY = stringPreferencesKey("user_email")
//    }
//
//    //get the saved email
//    val getEmail: Flow<String?> = context.dataStoree.data
//        .map { preferences ->
//            preferences[USER_EMAIL_KEY] ?: "FirstLast@gmail.com"
//        }
//
//    //save email into datastore
//    suspend fun saveEmail(name: String) {
//        context.dataStoree.edit { preferences ->
//            preferences[USER_EMAIL_KEY] = name
//        }
//    }
//
//
//}