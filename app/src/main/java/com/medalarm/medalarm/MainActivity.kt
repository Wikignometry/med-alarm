package com.medalarm.medalarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Button
import androidx.compose.material.Text
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.medalarm.medalarm.ui.theme.MedAlarmTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedAlarmTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Column{
                        ToastButton("I just took my pill", "Time logged!")
                    }

                }
            }
        }
    }
}



@Composable
fun ToastButton(label: String, text: String) {
    val context = LocalContext.current
    Button(onClick = {
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(context, text, duration)
        toast.show()
    }) {
        Text(label)
    }
}