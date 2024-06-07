package com.example.myapplication

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class PomodoroActivity : AppCompatActivity() {

    // Declare UI elements
    private lateinit var focusTimeSeekBar: SeekBar
    private lateinit var breakTimeSeekBar: SeekBar
    private lateinit var focusTimeText: TextView
    private lateinit var breakTimeText: TextView
    private lateinit var sessionsCount: TextView
    private lateinit var startButton: ImageButton
    private lateinit var pauseButton: Button
    private lateinit var resumeButton: Button
    private lateinit var stopButton: Button
    private lateinit var countdownText: TextView
    private lateinit var increaseSessionButton: ImageButton
    private lateinit var decreaseSessionsButton: ImageButton

    // Declare variables for managing timer and session state
    private var focusTimeInMillis: Long = 3600000
    private var breakTimeInMillis: Long = 1200000
    private var sessionCount = 4
    private var timer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 0
    private var timerPaused = false
    private var isFocusTime = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_focus_pomodoro)

        // Set up navigation button to another activity
        findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            startActivity(Intent(this, FocusActivity::class.java))
        }

        val stoptab: ImageButton = findViewById(R.id.stoptab)
        stoptab.setOnClickListener {
            val intent = Intent(this, StopActivity::class.java)
            startActivity(intent)
        }

        // Set up home button to navigate back to main activity
        val homeBut: ImageButton = findViewById(R.id.home2_ico)
        homeBut.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            this@PomodoroActivity.overridePendingTransition(
                R.anim.animate_zoom_enter,
                R.anim.animate_zoom_exit
            )
        }

        // Hide the action bar
        supportActionBar?.hide()

        // Initialize UI elements
        focusTimeSeekBar = findViewById(R.id.focusTimeSeekBar)
        breakTimeSeekBar = findViewById(R.id.breakTimeSeekBar)
        focusTimeText = findViewById(R.id.focusTimeText)
        breakTimeText = findViewById(R.id.breakTimeText)
        sessionsCount = findViewById(R.id.sessionsCount)
        startButton = findViewById(R.id.startButton)
        pauseButton = findViewById(R.id.pauseButton)
        increaseSessionButton = findViewById(R.id.increaseSessionsButton)
        decreaseSessionsButton = findViewById(R.id.decreaseSessionsButton)
        resumeButton = findViewById(R.id.resumeButton)
        stopButton = findViewById(R.id.stopButton)
        countdownText = findViewById(R.id.countdownText)

        // Set max values and initial progress for seek bars
        focusTimeSeekBar.max = 120
        focusTimeSeekBar.progress = 60
        breakTimeSeekBar.max = 60
        breakTimeSeekBar.progress = 20

        // Set up focus time seek bar change listener
        focusTimeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                focusTimeInMillis = progress * 60000L
                focusTimeText.text = formatTime(focusTimeInMillis)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Set up break time seek bar change listener
        breakTimeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                breakTimeInMillis = progress * 60000L
                breakTimeText.text = formatTime(breakTimeInMillis)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Set up session buttons to increase and decrease session count
        increaseSessionButton.setOnClickListener {
            sessionCount++
            sessionsCount.text = sessionCount.toString()
        }

        decreaseSessionsButton.setOnClickListener {
            if (sessionCount > 1) {
                sessionCount--
                sessionsCount.text = sessionCount.toString()
            }
        }

        // Set up start button to initiate Pomodoro timer
        startButton.setOnClickListener {
            toggleUIElements(false)
            startPomodoroTimer()
        }

        // Set up pause, resume, and stop buttons
        pauseButton.setOnClickListener {
            pauseTimer()
        }

        resumeButton.setOnClickListener {
            resumeTimer()
        }

        stopButton.setOnClickListener {
            stopTimer()
        }

        // Create notification channel for notifications
        createNotificationChannel()
    }

    // Toggle visibility of UI elements based on timer state
    private fun toggleUIElements(show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        focusTimeSeekBar.visibility = visibility
        breakTimeSeekBar.visibility = visibility
        focusTimeText.visibility = visibility
        breakTimeText.visibility = visibility
        sessionsCount.visibility = visibility
        startButton.visibility = visibility
        findViewById<View>(R.id.setFocusTimeLabel).visibility = visibility
        findViewById<View>(R.id.setBreakTimeLabel).visibility = visibility
        findViewById<View>(R.id.sessionsLabel).visibility = visibility
        increaseSessionButton.visibility = visibility
        decreaseSessionsButton.visibility = visibility
        countdownText.visibility = if (show) View.GONE else View.VISIBLE
        pauseButton.visibility = if (show) View.GONE else View.VISIBLE
        resumeButton.visibility = if (show) View.GONE else View.GONE
        stopButton.visibility = if (show) View.GONE else View.VISIBLE
    }

    // Start the Pomodoro focus timer
    private fun startPomodoroTimer() {
        timeLeftInMillis = focusTimeInMillis
        timerPaused = false
        isFocusTime = true
        pauseButton.visibility = View.VISIBLE
        resumeButton.visibility = View.GONE
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (timerPaused) {
                    cancel()
                } else {
                    timeLeftInMillis = millisUntilFinished
                    countdownText.text = formatTime(millisUntilFinished)
                }
            }

            override fun onFinish() {
                showAlertDialog("Focus session completed! Take a break.", ::startBreakTimer)
                showNotification("Focus session completed! Take a break.")
            }
        }.start()
    }

    // Start the break timer after focus session
    private fun startBreakTimer() {
        timeLeftInMillis = breakTimeInMillis
        timerPaused = false
        isFocusTime = false
        pauseButton.visibility = View.VISIBLE
        resumeButton.visibility = View.GONE
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (timerPaused) {
                    cancel()
                } else {
                    timeLeftInMillis = millisUntilFinished
                    countdownText.text = formatTime(millisUntilFinished)
                }
            }

            override fun onFinish() {
                if (sessionCount > 1) {
                    sessionCount--
                    sessionsCount.text = sessionCount.toString()
                    showAlertDialog("Break session completed! Ready to focus again?", ::startPomodoroTimer)
                    showNotification("Break session completed! Ready to focus again?")
                } else {
                    showAlertDialog("Break session completed! All sessions are done.", ::resetToPomodoroTab)
                    showNotification("Break session completed! All sessions are done.")
                }
            }
        }.start()
    }

    // Pause the timer
    private fun pauseTimer() {
        timerPaused = true
        timer?.cancel()
        pauseButton.visibility = View.GONE
        resumeButton.visibility = View.VISIBLE
    }

    // Resume the timer from paused state
    private fun resumeTimer() {
        timerPaused = false
        resumeButton.visibility = View.GONE
        pauseButton.visibility = View.VISIBLE
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                countdownText.text = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                if (isFocusTime) {
                    showAlertDialog("Focus session completed! Take a break.", ::startBreakTimer)
                    showNotification("Focus session completed! Take a break.")
                } else {
                    if (sessionCount > 1) {
                        sessionCount--
                        sessionsCount.text = sessionCount.toString()
                        showAlertDialog("Break session completed! Ready to focus again?", ::startPomodoroTimer)
                        showNotification("Break session completed! Ready to focus again?")
                    } else {
                        showAlertDialog("Break session completed! All sessions are done.", ::resetToPomodoroTab)
                        showNotification("Break session completed! All sessions are done.")
                    }
                }
            }
        }.start()
    }

    // Stop the timer and reset UI elements
    private fun stopTimer() {
        timer?.cancel()
        toggleUIElements(true)
        pauseButton.visibility = View.GONE
        resumeButton.visibility = View.GONE
        stopButton.visibility = View.GONE
    }

    // Show an alert dialog with a message and a callback for positive button click
    private fun showAlertDialog(message: String, onPositiveButtonClick: () -> Unit) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                onPositiveButtonClick()
            }
            .setCancelable(false)
            .show()
    }

    // Reset UI elements to initial state
    private fun resetToPomodoroTab() {
        toggleUIElements(true)
    }

    // Show a notification with a message
    private fun showNotification(message: String) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            return
        }

        val intent = Intent(this, PomodoroActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, "pomodoroChannel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Pomodoro Timer")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }

    // Create a notification channel for Android O and above
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Pomodoro Timer Channel"
            val descriptionText = "Channel for Pomodoro Timer notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("pomodoroChannel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Format time in milliseconds to HH:mm:ss format
    private fun formatTime(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        val hours = (millis / (1000 * 60 * 60)) % 24
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    // Handle the result of permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                showNotification("Focus session completed! Take a break.")
            } else {
                showAlertDialog("Notification permission is required to receive notifications.", {})
            }
        }
    }
}
