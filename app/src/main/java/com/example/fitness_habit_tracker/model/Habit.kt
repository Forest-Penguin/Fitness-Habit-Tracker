package com.example.fitness_habit_tracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val timesPerWeek: Int,
    val notes: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
