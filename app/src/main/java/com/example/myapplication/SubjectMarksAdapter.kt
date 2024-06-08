package com.example.myapplication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.tracker.SubjectMarks

// Adapter class for managing a list of SubjectMarks objects in a RecyclerView
class SubjectMarksAdapter(private val subjectMarksList: List<SubjectMarks>) :
    RecyclerView.Adapter<SubjectMarksAdapter.SubjectMarksViewHolder>() {

    // Called when RecyclerView needs a new ViewHolder of the given type to represent an item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectMarksViewHolder {
        // Inflate the item_subject_marks layout to create a new View
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_subject_marks, parent, false)
        // Create and return a new instance of SubjectMarksViewHolder with the inflated view
        return SubjectMarksViewHolder(view)
    }

    // Called by RecyclerView to display the data at the specified position
    override fun onBindViewHolder(holder: SubjectMarksViewHolder, position: Int) {
        // Get the SubjectMarks object at the specified position
        val subjectMarks = subjectMarksList[position]
        // Set the subject TextView with the subject name from the SubjectMarks object
        holder.subjectTextView.text = subjectMarks.subject
        // Set the marks TextView with the marks from the SubjectMarks object, formatted as a percentage
        holder.marksTextView.text = "${subjectMarks.marks}%"
        // Conditionally set the difference TextView with the difference from the SubjectMarks object
        holder.differenceTextView.text = if (subjectMarks.difference != 0) {
            // If the difference is positive, prefix with a plus sign
            if (subjectMarks.difference > 0) "+${subjectMarks.difference}%"
            // If the difference is negative or zero, just show the difference
            else "${subjectMarks.difference}%"
        } else {
            // If the difference is zero, set the text to an empty string
            ""
        }

        // Set the color of the differenceTextView based on the value of difference
        if (subjectMarks.difference > 0) {
            holder.differenceTextView.setTextColor(Color.GREEN)
        } else if (subjectMarks.difference < 0) {
            holder.differenceTextView.setTextColor(Color.RED)
        } else {
            holder.differenceTextView.setTextColor(Color.BLACK)
        }
    }

    // Returns the total number of items in the data set held by the adapter
    override fun getItemCount(): Int {
        // Return the size of the subjectMarksList
        return subjectMarksList.size
    }

    // ViewHolder class that describes an item view and metadata about its place within the RecyclerView
    class SubjectMarksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TextView for displaying the subject name
        val subjectTextView: TextView = itemView.findViewById(R.id.subjectTextView)
        // TextView for displaying the marks
        val marksTextView: TextView = itemView.findViewById(R.id.marksTextView)
        // TextView for displaying the difference in marks
        val differenceTextView: TextView = itemView.findViewById(R.id.differenceTextView)
    }
}
