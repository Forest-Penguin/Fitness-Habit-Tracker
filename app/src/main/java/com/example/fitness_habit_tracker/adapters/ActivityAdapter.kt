package com.example.fitness_habit_tracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness_habit_tracker.R
import com.example.fitness_habit_tracker.model.ActivityEntity
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.util.Locale

class ActivityAdapter : ListAdapter<ActivityEntity, ActivityAdapter.ViewHolder>(ActivityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeText: TextView = itemView.findViewById(R.id.tvActivityType)
        private val timeText: TextView = itemView.findViewById(R.id.tvActivityTime)
        private val durationText: TextView = itemView.findViewById(R.id.tvDuration)
        private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")

        fun bind(activity: ActivityEntity) {
            typeText.text = activity.type.name.lowercase().replaceFirstChar { it.uppercase() }
            timeText.text = activity.startTime.format(dateFormatter)
            
            val duration = if (activity.endTime != null) {
                Duration.between(activity.startTime, activity.endTime)
            } else {
                Duration.ofMillis(activity.duration)
            }

            val hours = duration.toHours()
            val minutes = (duration.toMinutes() % 60)
            val seconds = (duration.seconds % 60)

            durationText.text = when {
                hours > 0 -> String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
                else -> String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
            }
        }
    }

    private class ActivityDiffCallback : DiffUtil.ItemCallback<ActivityEntity>() {
        override fun areItemsTheSame(oldItem: ActivityEntity, newItem: ActivityEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ActivityEntity, newItem: ActivityEntity): Boolean {
            return oldItem == newItem
        }
    }
} 