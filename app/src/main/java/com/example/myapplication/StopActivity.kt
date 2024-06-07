package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.myapplication.databinding.ActivityFocusStopwatchBinding
import java.util.concurrent.TimeUnit

class StopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFocusStopwatchBinding
    private var timeInSeconds = 0L
    private var isRunning = false
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                timeInSeconds++
                updateStopwatchView()
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFocusStopwatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<ImageButton>(R.id.pomotab).setOnClickListener {
            startActivity(Intent(this, PomodoroActivity::class.java))
        }

        findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            startActivity(Intent(this, FocusActivity::class.java))
        }

        val settingtab: ImageButton = findViewById(R.id.set_ico)
        settingtab.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        val homebutton: ImageButton = findViewById(R.id.home2_ico)
        homebutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val button7: ImageButton = findViewById(R.id.art_ico)
        button7.setOnClickListener {
            comingsoon()
        }

        supportActionBar?.hide()

        binding.startButton.setOnClickListener {
            startStopwatch()
        }

        binding.stopButton.setOnClickListener {
            stopStopwatch()
        }

        binding.pauseButton.setOnClickListener {
            pauseStopwatch()
        }

        binding.resumeButton.setOnClickListener {
            resumeStopwatch()
        }
    }

    private fun comingsoon() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Soooo Sorry!")
        builder.setMessage("We know you're excited to use this feature, but it's still under heavy development. Please check back later.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun startStopwatch() {
        isRunning = true
        timeInSeconds = 0L
        updateStopwatchView()
        handler.post(runnable)

        binding.startButton.isVisible = false
        binding.stopButton.isVisible = true
        binding.pauseButton.isVisible = true
        binding.resumeButton.isVisible = false
        binding.stopwatchTime.isVisible = true
    }

    private fun stopStopwatch() {
        isRunning = false
        handler.removeCallbacks(runnable)
        timeInSeconds = 0L
        updateStopwatchView()

        binding.startButton.isVisible = true
        binding.stopButton.isVisible = false
        binding.pauseButton.isVisible = false
        binding.resumeButton.isVisible = false
        binding.stopwatchTime.isVisible = false
    }

    private fun pauseStopwatch() {
        isRunning = false
        handler.removeCallbacks(runnable)

        binding.pauseButton.isVisible = false
        binding.resumeButton.isVisible = true
        binding.stopButton.isVisible = true // Ensure stop button remains visible
    }

    private fun resumeStopwatch() {
        isRunning = true
        handler.post(runnable)

        binding.pauseButton.isVisible = true
        binding.resumeButton.isVisible = false
        binding.stopButton.isVisible = true // Ensure stop button remains visible
    }

    private fun updateStopwatchView() {
        val hours = TimeUnit.SECONDS.toHours(timeInSeconds)
        val minutes = TimeUnit.SECONDS.toMinutes(timeInSeconds) % 60
        val seconds = timeInSeconds % 60
        binding.stopwatchTime.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
