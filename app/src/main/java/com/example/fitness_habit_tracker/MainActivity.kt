package com.example.fitness_habit_tracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.fitness_habit_tracker.R
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fitness_habit_tracker.ui.theme.FitnessHabitTrackerTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // ✅ Ensure `activity_main.xml` exists

        val startTrackingButton = findViewById<Button>(R.id.startTrackingButton)
        val stopTrackingButton = findViewById<Button>(R.id.stopTrackingButton)

        startTrackingButton.setOnClickListener {
            startService(Intent(this, MotionSensorService::class.java))
        }

        stopTrackingButton.setOnClickListener {
            stopService(Intent(this, MotionSensorService::class.java))
        }
    }
}