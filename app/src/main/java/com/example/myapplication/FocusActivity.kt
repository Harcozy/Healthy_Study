
package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.CountdownScreen
import com.example.myapplication.ui.InputScreen2
import com.example.myapplication.ui.theme.MyTheme

enum class Screen {
    Input, Countdown
}

class FocusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_focus)

        val composeView = findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            MyTheme(darkTheme = false) {
                MyApp()
            }
        }
    }
}

@Preview
@Composable
fun MyApp() {

    var timeInSec = 0

    Surface(color = MaterialTheme.colors.background) {
        var screen by remember { mutableStateOf(Screen.Input) }

        Crossfade(targetState = screen, label = "") {
            when (it) {
                Screen.Input -> InputScreen2 {
                    timeInSec = it
                    screen = Screen.Countdown
                }
                Screen.Countdown -> CountdownScreen(timeInSec) {
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
