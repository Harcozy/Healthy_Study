package com.example.myapplication.tracker

data class SubjectMarks(
    val subject: String,
    var marks: Int,
    var difference: Int = 0
)

