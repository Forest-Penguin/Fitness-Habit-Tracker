package com.example.fitness_habit_tracker.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.example.fitness_habit_tracker.R
import com.example.fitness_habit_tracker.adapters.GoalAdapter
import com.example.fitness_habit_tracker.database.WorkoutDatabase
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.time.Duration
import com.example.fitness_habit_tracker.model.ActivityEntity
import com.example.fitness_habit_tracker.model.ActivityType
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.widget.LinearLayout
import android.graphics.Color


class GoalsActivity : AppCompatActivity() {
    companion object {
        const val ADD_GOAL_REQUEST = 101
    }
    private lateinit var goalAdapter: GoalAdapter
    private lateinit var db: WorkoutDatabase
    private lateinit var database: WorkoutDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

        database = WorkoutDatabase.getDatabase(this)

        lifecycleScope.launch {
            database.goalDao().deleteAllGoals()
            database.habitDao().deleteAllHabits()
        }

        findViewById<Button>(R.id.btnAddEditGoals).setOnClickListener {
            startActivity(Intent(this, AddGoalActivity::class.java))
        }

        findViewById<Button>(R.id.btnAddEditHabits).setOnClickListener {
            startActivity(Intent(this, AddHabitActivity::class.java))
        }


        // Bottom Navigation Button Setup
        val btnDashboard = findViewById<Button>(R.id.navDashboard)
        val btnTraining = findViewById<Button>(R.id.navTraining)
        val btnGoals = findViewById<Button>(R.id.navGoals)
        val btnHistory = findViewById<Button>(R.id.navHistory)
        val btnRecommendations = findViewById<Button>(R.id.navRecommendations)

        btnDashboard.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        btnTraining.setOnClickListener {
            startActivity(Intent(this, TrainingActivity::class.java))
        }

        btnGoals.setOnClickListener {
            // Already on Goals, do nothing
        }

        btnHistory.setOnClickListener {
            startActivity(Intent(this, ActivityHistoryActivity::class.java))
        }

        btnRecommendations.setOnClickListener {
            startActivity(Intent(this, ActivityRecommendationsActivity::class.java))
        }


    }

    override fun onResume() {
        super.onResume()
        loadGoalProgress()
        loadHabits()
    }

    private fun loadGoalProgress() {
        lifecycleScope.launch {
            val today = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)

            val allGoals = database.goalDao().getGoalsCreatedToday().filter { !it.completed }

            val activities = database.activityDao().getActivitiesSince(today)

            val stats = activities.groupBy { it.type }
                .mapValues { entry ->
                    val acts: List<ActivityEntity> = entry.value
                    acts.sumOf { act ->
                        val end = act.endTime ?: LocalDateTime.now()
                        Duration.between(act.startTime, end).seconds
                    }
                }

            fun format(seconds: Long): String {
                val minutes = seconds / 60
                val sec = seconds % 60
                return "$minutes min $sec sec"
            }

            val displayBuilder = StringBuilder()

            val latestGoalsByType = allGoals
                .groupBy { it.activityType }
                .mapValues { (_, goals) ->
                    goals.maxByOrNull { it.createdAt }
                }

            if (allGoals.isEmpty()) {
                findViewById<TextView>(R.id.tvGoalSummary).text = "No goals yet."
                return@launch
            }

            for ((activityType, goal) in latestGoalsByType) {
                if (goal != null) {
                    val actualSecs = stats.getOrDefault(ActivityType.valueOf(activityType), 0)
                    val actualFormatted = format(actualSecs)
                    val targetFormatted = "${goal.targetValue.toInt()} min"

                    when (activityType) {
                        "WALKING" -> displayBuilder.append("🚶 Walking Goal: $actualFormatted / $targetFormatted\n")
                        "RUNNING" -> displayBuilder.append("🏃 Running Goal: $actualFormatted / $targetFormatted\n")
                        "CYCLING" -> displayBuilder.append("🚴 Cycling Goal: $actualFormatted / $targetFormatted\n")
                        "STATIONARY" -> displayBuilder.append("🏋 Stationary Goal: $actualFormatted / $targetFormatted\n")
                    }
                }
            }


            findViewById<TextView>(R.id.tvGoalSummary).text = displayBuilder.toString().trim()
        }
    }

    private fun loadHabits() {
        lifecycleScope.launch {
            val habits = database.habitDao().getAllHabits()

            val container = findViewById<LinearLayout>(R.id.habitsContainer)
            container.removeAllViews()

            if (habits.isEmpty()) {
                val noText = TextView(this@GoalsActivity)
                noText.text = "No habits yet."
                noText.setTextColor(Color.BLACK)
                noText.textSize = 16f
                container.addView(noText)
            } else {
                for (habit in habits) {
                    val textView = TextView(this@GoalsActivity)
                    textView.text = "📝 ${habit.name} • ${habit.timesPerWeek}x /week"
                    textView.textSize = 16f
                    textView.setTextColor(Color.BLACK)
                    textView.setPadding(0, 8, 0, 8)
                    container.addView(textView)
                }
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_GOAL_REQUEST && resultCode == RESULT_OK && data != null) {
            val walking = data.getFloatExtra("goal_walking", 0f)
            val running = data.getFloatExtra("goal_running", 0f)
            val cycling = data.getFloatExtra("goal_cycling", 0f)
            val stationary = data.getFloatExtra("goal_stationary", 0f)

            val goalText = """
            🚶 Walking Goal: 0 / ${walking.toInt()} min
            🏃 Running Goal: 0 / ${running.toInt()} min
            🚴 Cycling Goal: 0 / ${cycling.toInt()} min
            🏋 Stationary Goal: 0 / ${stationary.toInt()} min
        """.trimIndent()

            findViewById<TextView>(R.id.tvGoalSummary).text = goalText
        }
    }
}
