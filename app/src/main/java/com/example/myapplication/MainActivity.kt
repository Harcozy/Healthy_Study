package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()


        setupDateTimeDisplay()
        val textView3: TextView = findViewById(R.id.textView3)
        textView3.text = "${getGreeting()}, ${getUsername()}"

        fetchQuote()

        val settingtab: ImageButton = findViewById(R.id.set_ico)
        settingtab.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        val button: ImageButton = findViewById(R.id.imageButton)
        button.setOnClickListener {
            val intent = Intent(this, FocusActivity::class.java)
            startActivity(intent)
        }

        val button2: ImageButton = findViewById(R.id.imageButton3)
        button2.setOnClickListener {
            val intent = Intent(this, TrackActivity::class.java)
            startActivity(intent)
        }

        val button3: ImageButton = findViewById(R.id.imageButton2)
        button3.setOnClickListener {
            comingsoon()
        }

        val button4: ImageButton = findViewById(R.id.imageButton4)
        button4.setOnClickListener {
            comingsoon()
        }

        val button5: ImageButton = findViewById(R.id.imageButton5)
        button5.setOnClickListener {
            comingsoon()
        }

        val button6: ImageButton = findViewById(R.id.imageButton6)
        button6.setOnClickListener {
            comingsoon()
        }

        val button7: ImageButton = findViewById(R.id.art_ico)
        button7.setOnClickListener {
            comingsoon()
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

    private fun setupDateTimeDisplay() {
        val tvDate = findViewById<TextView>(R.id.tvDate)
        val currentDate = Calendar.getInstance().time

        val dateFormat = SimpleDateFormat("d'${getDayOfMonthSuffix(currentDate.date)}' MMMM, yyyy", Locale.getDefault())
        tvDate.text = dateFormat.format(currentDate)
    }

    private fun getDayOfMonthSuffix(n: Int): String {
        if (n in 1..31) {
            if (n in 11..13) {
                return "th"
            }
            return when (n % 10) {
                1 -> "st"
                2 -> "nd"
                3 -> "rd"
                else -> "th"
            }
        }
        return ""
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

    private fun fetchQuote() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    NetworkService.quotesApi.getEducationQuotes()
                }
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val quotes = response.body()!!
                        val randomQuote = quotes.randomOrNull()
                        findViewById<TextView>(R.id.tvQuote).text = randomQuote?.let {
                            "\"${it.quote}\" - ${it.author ?: "Unknown"}"
                        } ?: "No quotes available."
                    } else {
                        findViewById<TextView>(R.id.tvQuote).text = getString(R.string.fetch_quotes_failed)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    findViewById<TextView>(R.id.tvQuote).text = getString(R.string.error_message, e.localizedMessage)
                }
            }
        }
    }

    private fun updateQuote(quote: Quote) {
        val tvQuote: TextView = findViewById(R.id.tvQuote)
        tvQuote.text = "\"${quote.quote}\" - ${quote.author ?: "Unknown"}"

        // Store the quote and the timestamp in SharedPreferences
        val sharedPref = getSharedPreferences("AppPref", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("lastQuote", quote.quote)
            putLong("lastQuoteTime", System.currentTimeMillis())
            apply()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()

        this@MainActivity.overridePendingTransition(
            R.anim.animate_fade_enter,
            R.anim.animate_fade_exit
        )
    }
}

