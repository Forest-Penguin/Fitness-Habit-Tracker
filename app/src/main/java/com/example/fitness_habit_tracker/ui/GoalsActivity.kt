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

        db = WorkoutDatabase.getDatabase(this)

        val rvActiveGoals = findViewById<RecyclerView>(R.id.rvActiveGoals)
        rvActiveGoals.layoutManager = LinearLayoutManager(this)

        goalAdapter = GoalAdapter(emptyList()) { _ -> }

        rvActiveGoals.adapter = goalAdapter

        loadGoals()

        findViewById<Button>(R.id.btnAddEditGoal).setOnClickListener {
            startActivity(Intent(this, AddGoalActivity::class.java))
        }
    }

    private fun loadGoals() {
        db.goalDao().getAllGoals().observe(this) { allGoals ->
            val activeGoals = allGoals.filter { !it.completed }
            goalAdapter.updateGoals(activeGoals)
        }
    }


    override fun onResume() {
        super.onResume()
        loadGoals() // refresh goals when returning to this screen
    }
}

