package com.example.fitness_habit_tracker.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fitness_habit_tracker.R
import com.example.fitness_habit_tracker.model.ActivityType
import com.example.fitness_habit_tracker.service.ActivityRecognitionService
import com.google.android.material.button.MaterialButtonToggleGroup

class TrainingActivity : AppCompatActivity() {
    private lateinit var activityToggleGroup: MaterialButtonToggleGroup
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var statusText: TextView
    private var currentActivityType: ActivityType = ActivityType.WALKING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        // Initialize views
        activityToggleGroup = findViewById(R.id.activityToggleGroup)
        startButton = findViewById(R.id.btnStart)
        stopButton = findViewById(R.id.btnStop)
        statusText = findViewById(R.id.statusText)

        setupActivityToggleGroup()
        setupButtons()
    }

    private fun setupActivityToggleGroup() {
        activityToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                currentActivityType = when (checkedId) {
                    R.id.btnWalking -> ActivityType.WALKING
                    R.id.btnRunning -> ActivityType.RUNNING
                    R.id.btnCycling -> ActivityType.CYCLING
                    R.id.btnStationary -> ActivityType.STATIONARY
                    else -> ActivityType.WALKING
                }
            }
        }
    }

    private fun setupButtons() {
        startButton.setOnClickListener {
            startActivityRecording()
        }

        stopButton.setOnClickListener {
            stopActivityRecording()
        }

        // Initially hide stop button
        stopButton.visibility = View.GONE
    }

    private fun startActivityRecording() {
        val intent = Intent(this, ActivityRecognitionService::class.java).apply {
            action = ActivityRecognitionService.ACTION_START_TRACKING
            putExtra(ActivityRecognitionService.EXTRA_ACTIVITY_TYPE, currentActivityType.name)
        }
        startService(intent)

        // Update UI
        startButton.visibility = View.GONE
        stopButton.visibility = View.VISIBLE
        activityToggleGroup.isEnabled = false
        statusText.text = "Recording ${currentActivityType.name.lowercase()}..."
    }

    private fun stopActivityRecording() {
        val intent = Intent(this, ActivityRecognitionService::class.java).apply {
            action = ActivityRecognitionService.ACTION_STOP_TRACKING
        }
        startService(intent)

        // Update UI
        startButton.visibility = View.VISIBLE
        stopButton.visibility = View.GONE
        activityToggleGroup.isEnabled = true
        statusText.text = "Select an activity to start tracking"
    }
}