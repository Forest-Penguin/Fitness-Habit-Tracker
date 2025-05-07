package com.example.fitness_habit_tracker.ui

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fitness_habit_tracker.R
import com.example.fitness_habit_tracker.model.ActivityType
import com.example.fitness_habit_tracker.service.ActivityRecognitionService

class TrainingActivity : AppCompatActivity() {
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var statusText: TextView
    private lateinit var btnWalking: Button
    private lateinit var btnRunning: Button
    private lateinit var btnCycling: Button
    private lateinit var btnStationary: Button
    private lateinit var chronometer: Chronometer

    private var currentActivityType: ActivityType = ActivityType.WALKING
    private var isRunning = false
    private var pauseOffset: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        // Initialize UI
        startButton = findViewById(R.id.btnStart)
        stopButton = findViewById(R.id.btnStop)
        statusText = findViewById(R.id.statusText)
        chronometer = findViewById(R.id.chronometer)

        btnWalking = findViewById(R.id.btnWalking)
        btnRunning = findViewById(R.id.btnRunning)
        btnCycling = findViewById(R.id.btnCycling)
        btnStationary = findViewById(R.id.btnStationary)

        setupActivityButtons()
        setupStartStopButtons()

        // Bottom navigation setup
        findViewById<Button>(R.id.navDashboard).setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
        findViewById<Button>(R.id.navTraining).setOnClickListener {
            // Already here
        }
        findViewById<Button>(R.id.navGoals).setOnClickListener {
            startActivity(Intent(this, GoalsActivity::class.java))
        }
        findViewById<Button>(R.id.navHistory).setOnClickListener {
            startActivity(Intent(this, ActivityHistoryActivity::class.java))
        }
        findViewById<Button>(R.id.navRecommendations).setOnClickListener {
            startActivity(Intent(this, ActivityRecommendationsActivity::class.java))
        }
    }

    private fun setupActivityButtons() {
        val activityClickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.btnWalking -> currentActivityType = ActivityType.WALKING
                R.id.btnRunning -> currentActivityType = ActivityType.RUNNING
                R.id.btnCycling -> currentActivityType = ActivityType.CYCLING
                R.id.btnStationary -> currentActivityType = ActivityType.STATIONARY
            }
            statusText.text = "Selected: ${currentActivityType.name.lowercase()}"
        }

        btnWalking.setOnClickListener(activityClickListener)
        btnRunning.setOnClickListener(activityClickListener)
        btnCycling.setOnClickListener(activityClickListener)
        btnStationary.setOnClickListener(activityClickListener)
    }

    private fun setupStartStopButtons() {
        startButton.setOnClickListener {
            startActivityRecording()
        }

        stopButton.setOnClickListener {
            stopActivityRecording()
        }

        stopButton.visibility = View.GONE
    }

    private fun startActivityRecording() {
        val intent = Intent(this, ActivityRecognitionService::class.java).apply {
            action = ActivityRecognitionService.ACTION_START_TRACKING
            putExtra(ActivityRecognitionService.EXTRA_ACTIVITY_TYPE, currentActivityType.name)
        }
        startService(intent)

        startButton.visibility = View.GONE
        stopButton.visibility = View.VISIBLE

        // Disable all activity buttons
        setActivityButtonsEnabled(false)
        statusText.text = "Recording ${currentActivityType.name.lowercase()}..."

        // Start the timer
        if (!isRunning) {
            chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
            chronometer.start()
            isRunning = true
        }
    }

    private fun stopActivityRecording() {
        val intent = Intent(this, ActivityRecognitionService::class.java).apply {
            action = ActivityRecognitionService.ACTION_STOP_TRACKING
        }
        startService(intent)

        startButton.visibility = View.VISIBLE
        stopButton.visibility = View.GONE

        // Enable all activity buttons
        setActivityButtonsEnabled(true)
        statusText.text = "Select an activity to start tracking"

        // Stop the timer
        if (isRunning) {
            chronometer.stop()
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
            isRunning = false
        }
    }

    private fun setActivityButtonsEnabled(enabled: Boolean) {
        btnWalking.isEnabled = enabled
        btnRunning.isEnabled = enabled
        btnCycling.isEnabled = enabled
        btnStationary.isEnabled = enabled
    }
}
