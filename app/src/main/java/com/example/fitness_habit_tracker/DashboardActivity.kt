package com.example.fitness_habit_tracker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.fitness_habit_tracker.R
import androidx.activity.ComponentActivity


class DashboardActivity : ComponentActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Bottom Navigation
        bottomNavigation = findViewById(R.id.bottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_goals -> {
                    startActivity(Intent(this, GoalsActivity::class.java))
                    true
                }
                R.id.nav_dashboard -> true // Already on Dashboard
                R.id.nav_recommendations -> {
                    startActivity(Intent(this, RecommendationsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
