package com.medalarm.medalarm

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.fragment.app.FragmentActivity

@OptIn(ExperimentalMaterial3Api::class)
class AlarmActivity : FragmentActivity() {
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


        setContent {
            Text("Test")
        }
    }
}