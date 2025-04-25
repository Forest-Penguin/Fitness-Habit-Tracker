package com.example.fitness_habit_tracker.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fitness_habit_tracker.R
import com.example.fitness_habit_tracker.database.WorkoutDatabase
import com.example.fitness_habit_tracker.model.Goal
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class GoalsActivity : AppCompatActivity() {
    private lateinit var tilWalkingGoal: TextInputLayout
    private lateinit var tilRunningGoal: TextInputLayout
    private lateinit var tilCyclingGoal: TextInputLayout
    private lateinit var db: WorkoutDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

        // Initialize views
        tilWalkingGoal = findViewById(R.id.tilWalkingGoal)
        tilRunningGoal = findViewById(R.id.tilRunningGoal)
        tilCyclingGoal = findViewById(R.id.tilCyclingGoal)

        db = WorkoutDatabase.getDatabase(this)

        // Load existing goals
        loadGoals()

        findViewById<android.widget.Button>(R.id.btnSave).setOnClickListener {
            saveGoals()
        }
    }

    private fun loadGoals() {
        lifecycleScope.launch {
            // Get active goals for each activity type
            val walkingGoals = db.goalDao().getGoalsByActivityType("WALKING").value
            val runningGoals = db.goalDao().getGoalsByActivityType("RUNNING").value
            val cyclingGoals = db.goalDao().getGoalsByActivityType("CYCLING").value

            // Update UI with the most recent goal for each activity type
            walkingGoals?.firstOrNull()?.let { goal ->
                tilWalkingGoal.editText?.setText(goal.targetValue.toString())
            }
            runningGoals?.firstOrNull()?.let { goal ->
                tilRunningGoal.editText?.setText(goal.targetValue.toString())
            }
            cyclingGoals?.firstOrNull()?.let { goal ->
                tilCyclingGoal.editText?.setText(goal.targetValue.toString())
            }
        }
    }

    private fun saveGoals() {
        val walkingGoalValue = tilWalkingGoal.editText?.text.toString().toFloatOrNull()
        val runningGoalValue = tilRunningGoal.editText?.text.toString().toFloatOrNull()
        val cyclingGoalValue = tilCyclingGoal.editText?.text.toString().toFloatOrNull()

        if (walkingGoalValue == null || runningGoalValue == null || cyclingGoalValue == null) {
            Toast.makeText(this, "Please enter valid numbers for all goals", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            // Create or update goals for each activity type
            val now = LocalDateTime.now()
            val endDate = now.plusDays(1) // Goals are daily

            // Walking goal
            val walkingGoal = Goal(
                activityType = "WALKING",
                targetValue = walkingGoalValue,
                unit = Goal.UNIT_MINUTES,
                endDate = endDate
            )
            db.goalDao().insert(walkingGoal)

            // Running goal
            val runningGoal = Goal(
                activityType = "RUNNING",
                targetValue = runningGoalValue,
                unit = Goal.UNIT_MINUTES,
                endDate = endDate
            )
            db.goalDao().insert(runningGoal)

            // Cycling goal
            val cyclingGoal = Goal(
                activityType = "CYCLING",
                targetValue = cyclingGoalValue,
                unit = Goal.UNIT_MINUTES,
                endDate = endDate
            )
            db.goalDao().insert(cyclingGoal)

            Toast.makeText(this@GoalsActivity, "Goals updated successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
} 