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

    // Binding object to access UI elements
    private lateinit var binding: ActivityFocusStopwatchBinding
    // Variable to store the elapsed time in seconds
    private var timeInSeconds = 0L
    // Boolean flag to indicate if the stopwatch is running
    private var isRunning = false
    // Handler to manage the timing events on the main thread
    private val handler = Handler(Looper.getMainLooper())
    // Runnable that increments the time every second and updates the UI
    private val runnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                timeInSeconds++
                updateStopwatchView()
                handler.postDelayed(this, 1000)
            }
        }
    }

    // Called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFocusStopwatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listeners for various buttons to navigate to other activities
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

        // Hide the action bar
        supportActionBar?.hide()

        // Set click listeners for stopwatch control buttons
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

    // Function to show a "coming soon" dialog
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

    // Function to start the stopwatch
    private fun startStopwatch() {
        isRunning = true
        timeInSeconds = 0L
        updateStopwatchView()
        handler.post(runnable)

        // Update the visibility of the buttons and stopwatch display
        binding.startButton.isVisible = false
        binding.stopButton.isVisible = true
        binding.pauseButton.isVisible = true
        binding.resumeButton.isVisible = false
        binding.stopwatchTime.isVisible = true
    }

    // Function to stop the stopwatch
    private fun stopStopwatch() {
        isRunning = false
        handler.removeCallbacks(runnable)
        timeInSeconds = 0L
        updateStopwatchView()

        // Update the visibility of the buttons and stopwatch display
        binding.startButton.isVisible = true
        binding.stopButton.isVisible = false
        binding.pauseButton.isVisible = false
        binding.resumeButton.isVisible = false
        binding.stopwatchTime.isVisible = false
    }

    // Function to pause the stopwatch
    private fun pauseStopwatch() {
        isRunning = false
        handler.removeCallbacks(runnable)

        // Update the visibility of the buttons
        binding.pauseButton.isVisible = false
        binding.resumeButton.isVisible = true
        binding.stopButton.isVisible = true // Ensure stop button remains visible
    }

    // Function to resume the stopwatch from a paused state
    private fun resumeStopwatch() {
        isRunning = true
        handler.post(runnable)

        // Update the visibility of the buttons
        binding.pauseButton.isVisible = true
        binding.resumeButton.isVisible = false
        binding.stopButton.isVisible = true // Ensure stop button remains visible
    }

    // Function to update the stopwatch display with the current time
    private fun updateStopwatchView() {
        val hours = TimeUnit.SECONDS.toHours(timeInSeconds)
        val minutes = TimeUnit.SECONDS.toMinutes(timeInSeconds) % 60
        val seconds = timeInSeconds % 60
        binding.stopwatchTime.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
