package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.tracker.SubjectMarks
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Activity for tracking subject marks
class TrackActivity : AppCompatActivity() {

    // UI components for input and displaying data
    private lateinit var subjectInput: EditText
    private lateinit var marksInput: EditText
    private lateinit var addButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SubjectMarksAdapter
    private val subjectMarksList = mutableListOf<SubjectMarks>()

    // Called when the activity is starting
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_res)

        // Setting up home button to navigate to MainActivity
        val homebutton: ImageButton = findViewById(R.id.home2_ico)
        homebutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Setting up settings button to navigate to SettingActivity
        val settingtab: ImageButton = findViewById(R.id.set_ico)
        settingtab.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        // Setting up a button for a feature that is still in development
        val button7: ImageButton = findViewById(R.id.art_ico)
        button7.setOnClickListener {
            comingsoon()
        }

        // Hiding the action bar for this activity
        supportActionBar?.hide()

        // Initializing UI components
        subjectInput = findViewById(R.id.subjectInput)
        marksInput = findViewById(R.id.marksInput)
        addButton = findViewById(R.id.addButton)
        recyclerView = findViewById(R.id.recyclerView)

        // Setting up RecyclerView with SubjectMarksAdapter
        adapter = SubjectMarksAdapter(subjectMarksList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Loading saved subject marks dat
        loadSubjectMarks()

        // Adding an onClickListener to the add button
        addButton.setOnClickListener {
            addMarks()
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

    // Function to add or update marks for a subject
    private fun addMarks() {
        // Getting input from EditText fields
        val subject = subjectInput.text.toString()
        val marks = marksInput.text.toString().toInt()

        // Checking if the subject already exists in the list
        val existingSubject = subjectMarksList.find { it.subject == subject }
        if (existingSubject != null) {
            // If subject exists, update its marks and calculate the difference
            existingSubject.difference = marks - existingSubject.marks
            existingSubject.marks = marks
        } else {
            // If subject does not exist, add it to the list
            subjectMarksList.add(SubjectMarks(subject, marks))
        }

        // Notifying the adapter of data changes
        adapter.notifyDataSetChanged()
        // Saving the updated subject marks list
        saveSubjectMarks()
    }

    // Function to save the subject marks list to shared preferences
    private fun saveSubjectMarks() {
        // Getting shared preferences
        val sharedPref = getSharedPreferences("AppPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        // Converting the subject marks list to JSON
        val gson = Gson()
        val json = gson.toJson(subjectMarksList)
        // Storing the JSON string in shared preferences
        editor.putString("subjectMarksList", json)
        editor.apply()
    }

    // Function to load the subject marks list from shared preferences
    private fun loadSubjectMarks() {
        // Getting shared preferences
        val sharedPref = getSharedPreferences("AppPref", Context.MODE_PRIVATE)
        val gson = Gson()
        // Retrieving the JSON string from shared preferences
        val json = sharedPref.getString("subjectMarksList", null)
        val type = object : TypeToken<MutableList<SubjectMarks>>() {}.type
        if (json != null) {
            // Converting the JSON string back to a list of SubjectMarks
            val list: MutableList<SubjectMarks> = gson.fromJson(json, type)
            subjectMarksList.addAll(list)
        }
        // Notifying the adapter of data changes
        adapter.notifyDataSetChanged()
    }
}
