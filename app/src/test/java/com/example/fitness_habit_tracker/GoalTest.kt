package com.example.fitness_habit_tracker

import com.example.fitness_habit_tracker.model.Goal
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime

class GoalTest {

    @Test
    fun createGoal_holdsCorrectData(){
        val activityType = "Walking"
        val targetValue = 1000f
        val currentValue = 250f
        val unit = Goal.UNIT_STEPS
        val startDate = LocalDateTime.now().minusDays(1)
        val endDate = LocalDateTime.now().plusDays(7)
        val createdAt = LocalDateTime.now().minusHours(2)
        val completed = false
        val description = "Walk every day for 1 week."

        val goal = Goal(activityType = activityType, targetValue = targetValue, currentValue = currentValue, unit = unit, startDate = startDate, endDate = endDate, createdAt = createdAt, completed = completed, description = description)

        assertEquals(activityType, goal.activityType)
        assertEquals(targetValue, goal.targetValue)
        assertEquals(currentValue, goal.currentValue)
        assertEquals(unit, goal.unit)
        assertEquals(startDate, goal.startDate)
        assertEquals(endDate, goal.endDate)
        assertEquals(createdAt, goal.createdAt)
        assertEquals(completed, goal.completed)
        assertEquals(description, goal.description)
    }

    @Test
    fun getProgress_currentValueIsZero_returnsZero() {
        val goal = Goal(activityType = "Running", targetValue = 1000f, currentValue = 0f, unit = "steps", endDate = LocalDateTime.now().plusDays(1))

        val progress = goal.getProgress()

        assertEquals(0.0f, progress)
    }

    @Test
    fun getProgress_currentValueLessThanTarget_returnsCorrectPercentage() {
        val goal = Goal(activityType = "Running", targetValue = 1000f, currentValue = 500f, unit = "steps", endDate = LocalDateTime.now().plusDays(1))

        val progress = goal.getProgress()

        assertEquals(50.0f, progress)
    }

    @Test
    fun getProgress_currentValueEqualToTarget_returnsHundred() {
        val goal = Goal(activityType = "Running", targetValue = 1000f, currentValue = 1000f, unit = "steps", endDate = LocalDateTime.now().plusDays(1))

        val progress = goal.getProgress()

        assertEquals(100.0f, progress)
    }

    @Test
    fun getProgress_currentValueGreaterThanTarget_returnsHundred() {
        val goal = Goal(activityType = "Running", targetValue = 1000f, currentValue = 1500f, unit = "steps", endDate = LocalDateTime.now().plusDays(1))

        val progress = goal.getProgress()

        assertEquals(100.0f, progress)
    }

    @Test
    fun isExpired_endDateInPast_returnsTrue() {
        val pastDate = LocalDateTime.now().minusDays(1)
        val goal = Goal(activityType = "Running", targetValue = 1000f, currentValue = 500f, unit = "steps", endDate = pastDate)

        val isExpired = goal.isExpired()

        assertTrue(isExpired)
    }

    @Test
    fun isExpired_endDateInFuture_returnsFalse() {
        val futureDate = LocalDateTime.now().plusDays(1)
        val goal = Goal(activityType = "Running", targetValue = 1000f, currentValue = 500f, unit = "steps", endDate = futureDate)

        val isExpired = goal.isExpired()

        assertFalse(isExpired)
    }

    @Test
    fun isExpired_endDateIsNow_returnsFalse() {
        val now = LocalDateTime.now()
        val goal = Goal(activityType = "Running", targetValue = 1000f, currentValue = 500f, unit = "steps", endDate = now)

        val isExpired = goal.isExpired()

        assertFalse(isExpired)
    }

    @Test
    fun updateProgress_valueBelowTarget_doesNotMarkCompleted() {
        val goal = Goal(
            activityType = "Running",
            targetValue = 100f,
            currentValue = 0f,
            unit = "km",
            endDate = LocalDateTime.now().plusDays(1)
        )

        goal.updateProgress(50f)

        assertEquals(50f, goal.currentValue, 0.001f)
        assertFalse(goal.completed)
    }

    @Test
    fun updateProgress_valueMeetsOrExceedsTarget_marksCompleted() {
        val goal = Goal(
            activityType = "Running",
            targetValue = 100f,
            currentValue = 0f,
            unit = "km",
            endDate = LocalDateTime.now().plusDays(1)
        )

        goal.updateProgress(100f)

        assertEquals(100f, goal.currentValue, 0.001f)
        assertTrue(goal.completed)
    }
}
