package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SubjectMarksAdapter(private val subjectMarksList: List<SubjectMarks>) :
    RecyclerView.Adapter<SubjectMarksAdapter.SubjectMarksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectMarksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_subject_marks, parent, false)
        return SubjectMarksViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubjectMarksViewHolder, position: Int) {
        val subjectMarks = subjectMarksList[position]
        holder.subjectTextView.text = subjectMarks.subject
        holder.marksTextView.text = "${subjectMarks.marks}%"
        holder.differenceTextView.text = if (subjectMarks.difference != 0) {
            if (subjectMarks.difference > 0) "+${subjectMarks.difference}%" else "${subjectMarks.difference}%"
        } else {
            ""
        }
    }

    override fun getItemCount(): Int {
        return subjectMarksList.size
    }

    class SubjectMarksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectTextView: TextView = itemView.findViewById(R.id.subjectTextView)
        val marksTextView: TextView = itemView.findViewById(R.id.marksTextView)
        val differenceTextView: TextView = itemView.findViewById(R.id.differenceTextView)
    }
}

