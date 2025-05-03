package com.example.fitness_habit_tracker.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fitness_habit_tracker.R
import com.example.fitness_habit_tracker.database.WorkoutDatabase
import com.example.fitness_habit_tracker.model.ActivityEntity
import com.example.fitness_habit_tracker.model.ActivityType
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

        // Bottom Navigation Button Setup
        val btnDashboard = findViewById<Button>(R.id.navDashboard)
        val btnTraining = findViewById<Button>(R.id.navTraining)
        val btnGoals = findViewById<Button>(R.id.navGoals)
        val btnHistory = findViewById<Button>(R.id.navHistory)
        val btnRecommendations = findViewById<Button>(R.id.navRecommendations)

        btnDashboard.setOnClickListener {
            // Already on Dashboard, do nothing
        }

        btnTraining.setOnClickListener {
            startActivity(Intent(this, TrainingActivity::class.java))
        }

        btnGoals.setOnClickListener {
            startActivity(Intent(this, GoalsActivity::class.java))
        }

        btnHistory.setOnClickListener {
            startActivity(Intent(this, ActivityHistoryActivity::class.java))
        }

        btnRecommendations.setOnClickListener {
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
                    Duration.between(activity.startTime, endTime).toMinutes()
                }
            }

        findViewById<android.widget.TextView>(R.id.walkingValue)?.text =
            getString(R.string.activity_duration_format, stats.getOrDefault(ActivityType.WALKING, 0))
        findViewById<android.widget.TextView>(R.id.runningValue)?.text =
            getString(R.string.activity_duration_format, stats.getOrDefault(ActivityType.RUNNING, 0))
        findViewById<android.widget.TextView>(R.id.cyclingValue)?.text =
            getString(R.string.activity_duration_format, stats.getOrDefault(ActivityType.CYCLING, 0))
        findViewById<android.widget.TextView>(R.id.stationaryValue)?.text =
            getString(R.string.activity_duration_format, stats.getOrDefault(ActivityType.STATIONARY, 0))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
