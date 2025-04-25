package com.example.fitness_habit_tracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val activityType: String,
    val targetValue: Float,
    var currentValue: Float = 0f,
    val unit: String,
    val startDate: LocalDateTime = LocalDateTime.now(),
    val endDate: LocalDateTime,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var completed: Boolean = false,
    val description: String? = null
) {
    companion object {
        const val UNIT_STEPS = "steps"
        const val UNIT_KILOMETERS = "km"
        const val UNIT_MINUTES = "min"
        const val UNIT_CALORIES = "kcal"
    }

    fun getProgress(): Float {
        return (currentValue / targetValue) * 100
    }

    fun isExpired(): Boolean {
        return LocalDateTime.now().isAfter(endDate)
    }

    fun updateProgress(value: Float) {
        currentValue = value
        if (currentValue >= targetValue) {
            completed = true
        }
    }
} 