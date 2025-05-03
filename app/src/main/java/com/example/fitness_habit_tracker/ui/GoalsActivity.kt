package com.example.fitness_habit_tracker.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.fitness_habit_tracker.R

class GoalsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

        // Navigate to AddGoalActivity when "Edit Goal" button is pressed
        findViewById<Button>(R.id.btnAddEditGoal).setOnClickListener {
            val intent = Intent(this, AddGoalActivity::class.java)
            startActivity(intent)
        }

        // You can later add logic here to load/display current or past goals if needed
    }
}
