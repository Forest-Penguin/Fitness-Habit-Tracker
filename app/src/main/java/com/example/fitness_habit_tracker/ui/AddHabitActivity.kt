package com.example.fitness_habit_tracker.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fitness_habit_tracker.R
import com.example.fitness_habit_tracker.database.WorkoutDatabase
import com.example.fitness_habit_tracker.model.Habit
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class AddHabitActivity : AppCompatActivity() {

    private lateinit var etHabitName: TextInputEditText
    private lateinit var etTimesPerWeek: TextInputEditText
    private lateinit var etNotes: TextInputEditText

    private lateinit var db: WorkoutDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_habit)

        // Initialize input fields
        etHabitName = findViewById(R.id.etHabitName)
        etTimesPerWeek = findViewById(R.id.etTimesPerWeek)
        etNotes = findViewById(R.id.etNotes)

        db = WorkoutDatabase.getDatabase(this)

        findViewById<MaterialButton>(R.id.btnSave).setOnClickListener {
            saveHabit()
        }
    }

    private fun saveHabit() {
        val name = etHabitName.text.toString().trim()
        val timesPerWeek = etTimesPerWeek.text.toString().toIntOrNull()
        val notes = etNotes.text.toString().trim()

        if (name.isEmpty() || timesPerWeek == null) {
            Toast.makeText(this, "Please enter a valid name and frequency", Toast.LENGTH_SHORT).show()
            return
        }

        val createdAt = LocalDateTime.now()

        val habit = Habit(
            name = name,
            timesPerWeek = timesPerWeek,
            notes = notes,
            createdAt = createdAt
        )

//        lifecycleScope.launch {
//            db.habitDao().insert(habit)
//            Toast.makeText(this@AddHabitActivity, "Habit saved successfully", Toast.LENGTH_SHORT).show()
//            finish()
//        }
    }
}
