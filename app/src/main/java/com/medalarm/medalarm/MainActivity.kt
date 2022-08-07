package com.medalarm.medalarm

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.fragment.app.FragmentActivity
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.medalarm.medalarm.ui.MainScreen
import com.medalarm.medalarm.ui.theme.MedAlarmTheme
import com.medalarm.medalarm.ui.theme.isLight


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

                MainScreen()
            }
        }
    }
}

// Extension functions are wonderful! https://stackoverflow.com/a/68423182
tailrec fun Context.getActivity(): FragmentActivity? = when (this) {
    is FragmentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}