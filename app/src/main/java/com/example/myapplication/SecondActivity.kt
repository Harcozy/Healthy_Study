package com.example.myapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupDateDisplay()
        val textView3: TextView = findViewById(R.id.textView3)
        textView3.text = "${getGreeting()}, ${getUsername()}"
    }

    private fun setupDateDisplay() {
        val tvDate = findViewById<TextView>(R.id.tvDate)
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd MM yyyy", Locale.getDefault())
        tvDate.text = dateFormat.format(currentDate)
    }

    private fun getGreeting(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 6..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..23 -> "Good Evening"
            else -> "Hello"
        }
    }
    private fun getUsername(): String {

        return "User"
    }
}