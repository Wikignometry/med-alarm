package com.medalarm.medalarm.ui.screens

import android.content.Intent
import android.text.format.DateFormat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlarm
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.medalarm.medalarm.AlarmActivity
import java.sql.Time
import java.text.SimpleDateFormat
import com.chargemap.compose.numberpicker.*
import com.medalarm.medalarm.ui.components.TimePickerButton
import com.medalarm.medalarm.util.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(initialCount: Int, initialEndTime: Time) {
    val context = LocalContext.current

    val composableScope = rememberCoroutineScope()

    var sdf = SimpleDateFormat("h:mm aa")
    if (DateFormat.is24HourFormat(context)) {
        sdf = SimpleDateFormat("H:mm")
    }

    val currMs = System.currentTimeMillis()
    var time by rememberSaveable { mutableStateOf(Time(currMs)) }
    var count by rememberSaveable { mutableStateOf(initialCount) }
    var endTime by rememberSaveable { mutableStateOf(initialEndTime) }

    var openDialog by rememberSaveable { mutableStateOf(false)  }
    var pickerValue by rememberSaveable { mutableStateOf(count)  }

    val textSize = 20.sp

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { setAlarms(context, time, endTime, count) },
                icon = { Icon(Icons.Default.AddAlarm, "Add alarms") },
                text = { Text(text = if (count > 1) "Create Alarms" else "Create Alarm") },
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
                        append("${count}")
                    }

                    append(" more times today")
                }, fontSize = textSize)
            }

            item {
                Button(onClick = {
                    openDialog = true
                }) {
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
                    composableScope.launch { saveEndTime(context, endTime) }
                }) {
                    Icon(Icons.Default.Edit, "Edit")
                    Text("Edit")
                }
            }

            // part 4
            item {
                Divider(modifier = Modifier.padding(16.dp))
            }

            item {
                Text("Debugging stuff :)")
            }

            item {
                Button(onClick = {
                    setAlarms(
                        context,
                        Time(System.currentTimeMillis()),
                        Time(System.currentTimeMillis() + 1000),
                        1
                    )
                }) {
                    Text("Trigger alarm")
                }
            }

            item {
                Button(onClick = {
                    context.startActivity(Intent(context, AlarmActivity::class.java))
                }) {
                    Text("Show alarm activity")
                }
            }
        }

        if (openDialog) {
            AlertDialog(
                onDismissRequest = {
                    openDialog = false
                },
                title = {
                    Text(text = "Select alarm count")
                },
                text = {
                        NumberPicker(
                            dividersColor = MaterialTheme.colorScheme.primary,
                            value = pickerValue,
                            range = 0..10,
                            onValueChange = {
                                pickerValue = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface)
                        )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openDialog = false
                            count = pickerValue
                            composableScope.launch { saveAlarmCount(context, count) }
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDialog = false
                        }
                    ) {
                        Text("CANCEL")
                    }
                },
            )
        }
    }
}