package com.medalarm.medalarm.ui

import android.text.format.DateFormat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlarmOff
import androidx.compose.material.icons.filled.Snooze
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.medalarm.medalarm.getDismissIntent
import com.medalarm.medalarm.getSnoozeIntent
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(receiverTime: Date) {
    val context = LocalContext.current
    var sdf = SimpleDateFormat("h:mm aa")
    if (DateFormat.is24HourFormat(context)) {
        sdf = SimpleDateFormat("H:mm")
    }

    Scaffold { paddingValues ->
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = paddingValues,
                modifier = Modifier.padding(32.dp).weight(1f)
            ) {
                item {
                    Text(sdf.format(receiverTime), fontSize = 48.sp)
                }

                item {
                    Text("It's time to take a pill!", fontSize = 20.sp)
                }
            }

            Button(
                onClick = { context.sendBroadcast(getSnoozeIntent()) },
                modifier = Modifier.size(width = 180.dp, height = 60.dp)
            ) {
                Icon(Icons.Default.Snooze, "Snooze")
                Text(text = "Snooze")
            }

            Button(
                onClick = { context.sendBroadcast(getDismissIntent()) },
            ) {
                Icon(Icons.Default.AlarmOff, "Alarm off")
                Text(text = "Dismiss")
            }

            Spacer(
                Modifier.weight(0.2f)
            )
        }
    }
}
