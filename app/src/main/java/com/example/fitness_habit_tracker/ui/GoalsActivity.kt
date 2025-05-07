package com.example.fitness_habit_tracker.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness_habit_tracker.R
import com.example.fitness_habit_tracker.adapters.GoalAdapter
import com.example.fitness_habit_tracker.database.WorkoutDatabase



class GoalsActivity : AppCompatActivity() {
    private lateinit var goalAdapter: GoalAdapter
    private lateinit var db: WorkoutDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

//        db = WorkoutDatabase.getDatabase(this)
//
//        val rvActiveGoals = findViewById<RecyclerView>(R.id.rvActiveGoals)
//        rvActiveGoals.layoutManager = LinearLayoutManager(this)
//
//        goalAdapter = GoalAdapter(emptyList()) { _ -> }
//
//        rvActiveGoals.adapter = goalAdapter
//
//        loadGoals()
//
        findViewById<Button>(R.id.btnAddEditGoals).setOnClickListener {
            startActivity(Intent(this, AddGoalActivity::class.java))
        }

        findViewById<Button>(R.id.btnAddEditHabits).setOnClickListener {
            startActivity(Intent(this, AddGoalActivity::class.java))
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
//
//    private fun loadGoals() {
//        db.goalDao().getAllGoals().observe(this) { allGoals ->
//            val activeGoals = allGoals.filter { !it.completed }
//            goalAdapter.updateGoals(activeGoals)
//        }
//    }
//
//
//    override fun onResume() {
//        super.onResume()
//        loadGoals() // refresh goals when returning to this screen
//    }
}
