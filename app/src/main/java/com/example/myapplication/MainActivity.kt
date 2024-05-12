package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        supportActionBar?.hide()

        findViewById<ImageButton>(R.id.startbut1).setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
            this@MainActivity.overridePendingTransition(
                R.anim.animate_zoom_enter,
                R.anim.animate_zoom_exit
            )
        } 
    }
}
