package com.medalarm.medalarm

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.fragment.app.FragmentActivity

@OptIn(ExperimentalMaterial3Api::class)
class AlarmActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold {
                Text("Test")
            }
        }
    }
}