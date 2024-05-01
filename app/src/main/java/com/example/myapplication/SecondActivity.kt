package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale



class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        setupDateTimeDisplay()
        val textView3: TextView = findViewById(R.id.textView3)
        textView3.text = "${getGreeting()}, ${getUsername()}"

        fetchQuote()
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
}

