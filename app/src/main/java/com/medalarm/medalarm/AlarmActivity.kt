package com.medalarm.medalarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.fragment.app.FragmentActivity
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.medalarm.medalarm.ui.AlarmScreen
import com.medalarm.medalarm.ui.theme.MedAlarmTheme
import com.medalarm.medalarm.ui.theme.isLight

class AlarmActivityReceiver(private val activity: AlarmActivity) : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        activity.finish()
        Log.d("medalarm","ended activity")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class AlarmActivity : FragmentActivity() {

    private val receiver = AlarmActivityReceiver(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }

        //set flags so activity is showed when phone is off (on lock screen)
        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val filter = IntentFilter("com.medalarm.onalarmend");
        registerReceiver(receiver, filter)

        setContent {
            MedAlarmTheme {
                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(
                    color = MaterialTheme.colorScheme.background,
                    darkIcons = MaterialTheme.colorScheme.isLight()
                )

                AlarmScreen()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}