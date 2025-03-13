package com.example.fitness_habit_tracker

import org.junit.Assert.assertEquals
import org.junit.Test

class WorkoutTrackerTest {

    fun calculateStreak(completedDays: List<Boolean>): Int {
        var streak = 0
        for (completed in completedDays.reversed()) {
            if (!completed) break
            streak++
        }
        return streak
    }

    @Test
    fun testCalculateStreak() {
        val completedDays = listOf(true, true, false, true, true, true)
        assertEquals(3, calculateStreak(completedDays)) // Last 3 workouts were completed

        val completedDays2 = listOf(false, false, false)
        assertEquals(0, calculateStreak(completedDays2)) // No streak at all

        val completedDays3 = listOf(true, true, true, true, true)
        assertEquals(5, calculateStreak(completedDays3)) // Full streak
    }
}
