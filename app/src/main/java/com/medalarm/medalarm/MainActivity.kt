package com.medalarm.medalarm

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.fragment.app.FragmentActivity
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.medalarm.medalarm.ui.screens.MainScreen
import com.medalarm.medalarm.ui.theme.MedAlarmTheme
import com.medalarm.medalarm.ui.theme.isLight
import com.medalarm.medalarm.util.loadAlarmCount
import com.medalarm.medalarm.util.loadEndTime
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.sql.Time


class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = applicationContext

        var count = 0
        var endTime = Time(System.currentTimeMillis())
        runBlocking {
            launch { count = loadAlarmCount(context) }
            launch { endTime = loadEndTime(context) }
        }

        setContent {
            MedAlarmTheme {
                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(
                    color = MaterialTheme.colorScheme.background,
                    darkIcons = MaterialTheme.colorScheme.isLight()
                )

                MainScreen(count, endTime)
            }
        }
    }
}