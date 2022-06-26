package com.medalarm.medalarm

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlarm
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.medalarm.medalarm.ui.theme.MedAlarmTheme
import com.medalarm.medalarm.ui.theme.isLight
import java.sql.Time
import java.text.SimpleDateFormat
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TimerScreen() {
    val context = LocalContext.current
    var sdf = SimpleDateFormat("h:mm aa")
    if (DateFormat.is24HourFormat(context)) {
        sdf = SimpleDateFormat("H:mm")
    }
    val currMs = System.currentTimeMillis()
    var time by rememberSaveable { mutableStateOf(Time(currMs)) }
    var count = rememberSaveable { mutableStateOf(3) } // TODO: Make this save across app restarts
    var endTime by rememberSaveable { mutableStateOf(Time(currMs)) } // TODO: Make this default to something reasonable and persist across app restarts

    val textSize = 20.sp

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /*TODO*/ },
                icon = { Icon(Icons.Default.AddAlarm, "Add alarms") },
                text = { Text(text = "Create Alarms") }, // TODO: make s conditional on alarm count
            )
        },
    ) { paddingValues ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = paddingValues,
            modifier = Modifier.padding(32.dp)
        ) {
            item {
                Text(buildAnnotatedString {
                    append("I first took my pill at ")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${sdf.format(time)}")
                    }
                }, fontSize = textSize)
            }

            item {
                TimePickerButton({ ms ->
                    time = Time(ms)
                }) {
                    Icon(Icons.Default.Edit, "Edit")
                    Text("Edit")
                }
            }

            item {
                Divider(modifier = Modifier.padding(16.dp))
            }

            // part 2

            item {
                Text(buildAnnotatedString {
                    append("I will take my pill ")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${count.value}")
                    }

                    append(" more times today")
                }, fontSize = textSize)
            }

            item {
                Button(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Edit, "Edit")
                    Text("Edit")
                }
            }

            item {
                Divider(modifier = Modifier.padding(16.dp))
            }

            // part 3

            item {
                Text(buildAnnotatedString {
                    append("My last pill will be taken at ")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${sdf.format(endTime)}")
                    }
                }, fontSize = textSize)
            }

            item {
                TimePickerButton({ ms ->
                    endTime = Time(ms)
                }) {
                    Icon(Icons.Default.Edit, "Edit")
                    Text("Edit")
                }
            }
        }
    }
}


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