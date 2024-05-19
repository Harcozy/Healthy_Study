package com.example.myapplication

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.CountdownScreen
import com.example.myapplication.ui.InputScreen2
import com.example.myapplication.ui.MY_PERMISSIONS_REQUEST_VIBRATE
import com.example.myapplication.ui.sendNotification
import com.example.myapplication.ui.theme.MyTheme

enum class Screen {
    Input, Countdown
}

class FocusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_focus)

        supportActionBar?.hide()

        val composeView = findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            MyTheme(darkTheme = false) {
                MyApp()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_VIBRATE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission was granted, you can now show the notification
                    sendNotification(this)
                } else {
                    // Permission was denied, handle accordingly
                    println("Permission denied!")
                }
                return
            }
            // Handle other permission results
        }
    }
}

@Preview
@Composable
fun MyApp() {
    var timeInSec by remember { mutableStateOf(0) }
    val context = LocalContext.current

    Surface(color = Color.Transparent) {
        var screen by remember { mutableStateOf(Screen.Input) }

        Crossfade(targetState = screen) {
            when (it) {
                Screen.Input -> InputScreen2 { inputTime ->
                    timeInSec = inputTime
                    screen = Screen.Countdown
                }
                Screen.Countdown -> CountdownScreen(timeInSec, context) {
                    screen = Screen.Input
                }
            }
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
