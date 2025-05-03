package com.example.fitness_habit_tracker.ui

import android.os.Bundle
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
    }

    private fun loadDashboardData() {
        lifecycleScope.launch {
            // Get today's activities
            val today = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)
            val activities = database.activityDao().getActivitiesSince(today)

            // Update UI with activity stats
            updateActivityStats(activities)
        }
    }

    private fun updateActivityStats(activities: List<ActivityEntity>) {
        // Calculate total duration for each activity type
        val stats = activities.groupBy { it.type }
            .mapValues { (_, activities) ->
                activities.sumOf { activity ->
                    val endTime = activity.endTime ?: LocalDateTime.now()
                    Duration.between(activity.startTime, endTime).toMinutes()
                }
            }

        // Update UI elements with stats using string resources
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