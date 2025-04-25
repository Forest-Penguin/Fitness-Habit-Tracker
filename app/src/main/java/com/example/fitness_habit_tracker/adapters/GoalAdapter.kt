package com.example.fitness_habit_tracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness_habit_tracker.R
import com.example.fitness_habit_tracker.model.Goal
import com.google.android.material.progressindicator.LinearProgressIndicator
import java.time.format.DateTimeFormatter

class GoalAdapter(
    private var goals: List<Goal>,
    private val onUpdateClick: (Goal) -> Unit
) : RecyclerView.Adapter<GoalAdapter.GoalViewHolder>() {

    class GoalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val goalTypeText: TextView = view.findViewById(R.id.tvGoalType)
        val progressText: TextView = view.findViewById(R.id.tvProgress)
        val progressBar: LinearProgressIndicator = view.findViewById(R.id.progressIndicator)
        val descriptionText: TextView = view.findViewById(R.id.tvDescription)
        val dateRangeText: TextView = view.findViewById(R.id.tvDateRange)
        val updateButton: Button = view.findViewById(R.id.btnUpdate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_goal, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = goals[position]
        val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

        holder.goalTypeText.text = goal.activityType
        holder.progressText.text = "${goal.currentValue}/${goal.targetValue}"
        holder.progressBar.progress = ((goal.currentValue / goal.targetValue) * 100).toInt()
        holder.descriptionText.text = goal.description ?: ""
        holder.dateRangeText.text = "${goal.startDate.format(dateFormatter)} - ${goal.endDate.format(dateFormatter)}"
        
        holder.updateButton.setOnClickListener {
            onUpdateClick(goal)
        }
    }

    override fun getItemCount() = goals.size

    fun updateGoals(newGoals: List<Goal>) {
        goals = newGoals
        notifyDataSetChanged()
    }
} 