package com.medalarm.medalarm

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import com.medalarm.medalarm.ui.theme.MedAlarmTheme
import kotlinx.coroutines.flow.map
import java.security.AccessController.getContext
import java.util.*
import java.util.concurrent.Flow
import java.util.prefs.Preferences


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
    var time by rememberSaveable { mutableStateOf("") }
    Column{
        TimePickerButton ("I already took my pill", "Time Logged!", time) { _, hour: Int, minute: Int ->
            time = "$hour:$minute"
        }
        Text("I first took my pill at $time")
    }

}

// from https://github.com/Kiran-Bahalaskar/Time-Picker-With-Jetpack-Compose/blob/master/app/src/main/java/com/kiranbahalaskar/timepicker/MainActivity.kt
@Composable
fun TimePickerButton(label: String, text: String, time: String, onValueChange: (TimePicker, Int, Int) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]
    val timePickerDialog = TimePickerDialog(
        context,
        onValueChange, hour, minute, false
    )
        Button(onClick = {
            timePickerDialog.show()
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(context, text, duration)
            toast.show()
        }) {
            Text(text = label)
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