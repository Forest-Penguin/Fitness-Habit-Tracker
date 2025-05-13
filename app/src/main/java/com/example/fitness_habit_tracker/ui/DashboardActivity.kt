package com.example.fitness_habit_tracker.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fitness_habit_tracker.R
import com.example.fitness_habit_tracker.database.WorkoutDatabase
import com.example.fitness_habit_tracker.model.ActivityEntity
import com.example.fitness_habit_tracker.model.ActivityType
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.time.Duration

class DashboardActivity : AppCompatActivity() {
    private lateinit var database: WorkoutDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        database = WorkoutDatabase.getDatabase(this)

        // Set up back navigation
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        // Load dashboard data
        loadDashboardData()

        // Bottom Navigation Buttons
        findViewById<MaterialButton>(R.id.navDashboard).setOnClickListener {
            // Already on Dashboard, do nothing
        }

        findViewById<MaterialButton>(R.id.navTraining).setOnClickListener {
            startActivity(Intent(this, TrainingActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.navGoals).setOnClickListener {
            startActivity(Intent(this, GoalsActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.navHistory).setOnClickListener {
            startActivity(Intent(this, ActivityHistoryActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.navRecommendations).setOnClickListener {
            startActivity(Intent(this, ActivityRecommendationsActivity::class.java))
        }
    }

    private fun loadDashboardData() {
        lifecycleScope.launch {
            val today = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)
            val activities = database.activityDao().getActivitiesSince(today)
            updateActivityStats(activities)
        }
    }

    private fun updateActivityStats(activities: List<ActivityEntity>) {
        val stats = activities.groupBy { it.type }
            .mapValues { (_, activities) ->
                activities.sumOf { activity ->
                    val endTime = activity.endTime ?: LocalDateTime.now()
                    Duration.between(activity.startTime, endTime).seconds
                }
            }
        // Convert seconds to minutes and seconds format
        fun formatDuration(seconds: Long): String {
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            return "$minutes min $remainingSeconds sec"
        }

        findViewById<android.widget.TextView>(R.id.walkingValue)?.text =
            formatDuration(stats.getOrDefault(ActivityType.WALKING, 0))

        findViewById<android.widget.TextView>(R.id.runningValue)?.text =
            formatDuration(stats.getOrDefault(ActivityType.RUNNING, 0))

        findViewById<android.widget.TextView>(R.id.cyclingValue)?.text =
            formatDuration(stats.getOrDefault(ActivityType.CYCLING, 0))

        findViewById<android.widget.TextView>(R.id.stationaryValue)?.text =
            formatDuration(stats.getOrDefault(ActivityType.STATIONARY, 0))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
