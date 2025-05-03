package com.example.fitness_habit_tracker.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fitness_habit_tracker.R
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class ActivityRecommendationsActivity : AppCompatActivity() {

    private lateinit var predictionText: TextView

    // This listens for the prediction result
    private val predictionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val prediction = intent?.getStringExtra("prediction") ?: "Unknown"
            predictionText.text = getString(R.string.prediction_template, prediction)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendations)

        predictionText = findViewById(R.id.prediction_text)

        val testButton = findViewById<Button>(R.id.test_prediction_button)
        testButton.setOnClickListener {
            val intent = Intent("com.example.fitness_habit_tracker.PREDICTION_RESULT")
            intent.putExtra("prediction", "RUNNING")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }

        // Register the broadcast receiver
        val filter = IntentFilter("com.example.fitness_habit_tracker.PREDICTION_RESULT")

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            LocalBroadcastManager.getInstance(this).registerReceiver(predictionReceiver, filter)

        } else {
            @Suppress("DEPRECATION")
            LocalBroadcastManager.getInstance(this).registerReceiver(predictionReceiver, filter)

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
            startActivity(Intent(this, GoalsActivity::class.java))
        }

        btnHistory.setOnClickListener {
            startActivity(Intent(this, ActivityHistoryActivity::class.java))
        }

        btnRecommendations.setOnClickListener {
            // Already on Recommendations, do nothing
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(predictionReceiver)
    }
}