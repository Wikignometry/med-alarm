package com.medalarm.medalarm.ui

import android.content.Intent
import android.text.format.DateFormat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlarm
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.medalarm.medalarm.AlarmActivity
import com.medalarm.medalarm.setAlarms
import java.sql.Time
import java.text.SimpleDateFormat
import com.chargemap.compose.numberpicker.*

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

    val openDialog = rememberSaveable { mutableStateOf(false)  }
    var pickerValue by rememberSaveable { mutableStateOf(0) }

    val textSize = 20.sp

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { setAlarms(context, time, endTime, count.value) },
                icon = { Icon(Icons.Default.AddAlarm, "Add alarms") },
                text = { Text(text = if (count.value > 1) "Create Alarms" else "Create Alarm") },
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
                Button(onClick = {
                    openDialog.value = true
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

        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
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
                            openDialog.value = false
                            count.value = pickerValue
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDialog.value = false
                        }
                    ) {
                        Text("CANCEL")
                    }
                },
            )
        }
    }
}