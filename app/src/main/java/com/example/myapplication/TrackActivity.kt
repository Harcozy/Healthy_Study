package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.tracker.SubjectMarks
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TrackActivity : AppCompatActivity() {

    private lateinit var subjectInput: EditText
    private lateinit var marksInput: EditText
    private lateinit var addButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SubjectMarksAdapter
    private val subjectMarksList = mutableListOf<SubjectMarks>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_res)

        supportActionBar?.hide()

        subjectInput = findViewById(R.id.subjectInput)
        marksInput = findViewById(R.id.marksInput)
        addButton = findViewById(R.id.addButton)
        recyclerView = findViewById(R.id.recyclerView)

        adapter = SubjectMarksAdapter(subjectMarksList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadSubjectMarks()

        addButton.setOnClickListener {
            addMarks()
        }
    }

    private fun addMarks() {
        val subject = subjectInput.text.toString()
        val marks = marksInput.text.toString().toInt()

        val existingSubject = subjectMarksList.find { it.subject == subject }
        if (existingSubject != null) {
            existingSubject.difference = marks - existingSubject.marks
            existingSubject.marks = marks
        } else {
            subjectMarksList.add(SubjectMarks(subject, marks))
        }

        adapter.notifyDataSetChanged()
        saveSubjectMarks()
    }

    private fun saveSubjectMarks() {
        val sharedPref = getSharedPreferences("AppPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()
        val json = gson.toJson(subjectMarksList)
        editor.putString("subjectMarksList", json)
        editor.apply()
    }

    private fun loadSubjectMarks() {
        val sharedPref = getSharedPreferences("AppPref", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("subjectMarksList", null)
        val type = object : TypeToken<MutableList<SubjectMarks>>() {}.type
        if (json != null) {
            val list: MutableList<SubjectMarks> = gson.fromJson(json, type)
            subjectMarksList.addAll(list)
        }
        adapter.notifyDataSetChanged()
    }
}
