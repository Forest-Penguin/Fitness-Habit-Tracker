package com.example.fitness_habit_tracker.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fitness_habit_tracker.R
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class ActivityRecommendationsActivity : AppCompatActivity() {

    private lateinit var predictionText: TextView

    private val predictionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val prediction = intent?.getStringExtra("prediction") ?: "Unknown"
            Log.d("UI", "Received prediction broadcast: $prediction")
            predictionText.text = getString(R.string.prediction_template, prediction)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendations)

        predictionText = findViewById(R.id.prediction_text)

        val testButton = findViewById<Button>(R.id.test_prediction_button)
        testButton.setOnClickListener {
            val prefs = getSharedPreferences("predictions", Context.MODE_PRIVATE)
            val latestPrediction = prefs.getString("latest_prediction", "No prediction yet")
            Log.d("UI", "Loaded saved prediction: $latestPrediction")
            predictionText.text = getString(R.string.prediction_template, latestPrediction)
        }

        // Bottom Navigation Buttons
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
            // Already here
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter("com.example.fitness_habit_tracker.PREDICTION_RESULT")
        LocalBroadcastManager.getInstance(this).registerReceiver(predictionReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(predictionReceiver)
    }
}
