package com.example.fitness_habit_tracker.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fitness_habit_tracker.R
import com.example.fitness_habit_tracker.database.WorkoutDatabase
import com.example.fitness_habit_tracker.model.Goal
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class AddGoalActivity : AppCompatActivity() {

    private lateinit var etWalkingGoal: TextInputEditText
    private lateinit var etRunningGoal: TextInputEditText
    private lateinit var etCyclingGoal: TextInputEditText

    private lateinit var db: WorkoutDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_goal)

        etWalkingGoal = findViewById(R.id.etWalkingGoal)
        etRunningGoal = findViewById(R.id.etRunningGoal)
        etCyclingGoal = findViewById(R.id.etCyclingGoal)

        db = WorkoutDatabase.getDatabase(this)

        findViewById<MaterialButton>(R.id.btnSave).setOnClickListener {
            saveGoals()
            startActivity(Intent(this, GoalsActivity::class.java))
        }
    }

    private fun saveGoals() {
        val walking = etWalkingGoal.text.toString().toFloatOrNull()
        val running = etRunningGoal.text.toString().toFloatOrNull()
        val cycling = etCyclingGoal.text.toString().toFloatOrNull()

        if (walking == null || running == null || cycling == null) {
            Toast.makeText(this, "Please enter valid numbers for all goals", Toast.LENGTH_SHORT).show()
            return
        }

        val endDate = java.time.LocalDateTime.now().plusDays(1)

        lifecycleScope.launch {
            db.goalDao().insert(
                Goal(
                    activityType = "WALKING",
                    targetValue = walking,
                    unit = Goal.UNIT_MINUTES,
                    endDate = endDate
                )
            )
            db.goalDao().insert(
                Goal(
                    activityType = "RUNNING",
                    targetValue = running,
                    unit = Goal.UNIT_MINUTES,
                    endDate = endDate
                )
            )
            db.goalDao().insert(
                Goal(
                    activityType = "CYCLING",
                    targetValue = cycling,
                    unit = Goal.UNIT_MINUTES,
                    endDate = endDate
                )
            )

            Toast.makeText(this@AddGoalActivity, "Goals saved successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
