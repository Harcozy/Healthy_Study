package com.example.myapplication

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.ImageButton
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

class FocusActivity() : AppCompatActivity(), Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_focus_countdown)

        supportActionBar?.hide()

        // Initialize Compose view
        val composeView = findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            MyTheme(darkTheme = false) {
                MyApp()
            }
        }

        findViewById<ImageButton>(R.id.pomotab).setOnClickListener {
            startActivity(Intent(this, PomodoroActivity::class.java))
        }

        val settingtab: ImageButton = findViewById(R.id.set_ico)
        settingtab.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        val stoptab: ImageButton = findViewById(R.id.stoptab)
        stoptab.setOnClickListener {
            val intent = Intent(this, StopActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.home2_ico).setOnClickListener {//This sets an onClickListener on the start button.
            startActivity(Intent(this, MainActivity::class.java))
            this@FocusActivity.overridePendingTransition(
                R.anim.animate_zoom_enter,
                R.anim.animate_zoom_exit
            )
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FocusActivity> {
        override fun createFromParcel(parcel: Parcel): FocusActivity {
            return FocusActivity(parcel)
        }

        override fun newArray(size: Int): Array<FocusActivity?> {
            return arrayOfNulls(size)
        }
    }
}

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
