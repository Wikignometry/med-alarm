package com.medalarm.medalarm.ui

import android.text.format.DateFormat
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlarmOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.medalarm.medalarm.getDismissIntent
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun AlarmScreen() {
    val context = LocalContext.current
    var sdf = SimpleDateFormat("h:mm aa")
    if (DateFormat.is24HourFormat(context)) {
        sdf = SimpleDateFormat("H:mm")
    }

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { context.sendBroadcast(getDismissIntent()) },
                icon = { Icon(Icons.Default.AlarmOff, "Alarm off") },
                text = { Text(text = "Dismiss") },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = paddingValues,
            modifier = Modifier.padding(32.dp)
        ) {
            item {
                // TODO: hardcode this to the broadcast receiver's trigger time
                Text(sdf.format(Date()), fontSize = 48.sp)
            }

            item {
                Text("It's time to take a pill!", fontSize = 20.sp)
            }
        }
    }
}
