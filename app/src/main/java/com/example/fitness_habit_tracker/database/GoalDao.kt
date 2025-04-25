package com.example.fitness_habit_tracker.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fitness_habit_tracker.model.Goal
import java.time.LocalDateTime

@Dao
interface GoalDao {
    @Insert
    suspend fun insert(goal: Goal)

    @Update
    suspend fun update(goal: Goal)

    @Delete
    suspend fun delete(goal: Goal)

    @Query("SELECT * FROM goals ORDER BY createdAt DESC")
    fun getAllGoals(): LiveData<List<Goal>>

    @Query("SELECT * FROM goals WHERE completed = 0 AND endDate > :now ORDER BY endDate ASC")
    fun getActiveGoals(now: LocalDateTime = LocalDateTime.now()): LiveData<List<Goal>>

    @Query("SELECT * FROM goals WHERE completed = 1 ORDER BY endDate DESC")
    fun getCompletedGoals(): LiveData<List<Goal>>

    @Query("SELECT * FROM goals WHERE activityType = :activityType AND completed = 0")
    fun getGoalsByActivityType(activityType: String): LiveData<List<Goal>>

    @Query("UPDATE goals SET currentValue = :newValue WHERE id = :goalId")
    suspend fun updateProgress(goalId: Long, newValue: Float)

    @Query("UPDATE goals SET completed = 1 WHERE id = :goalId")
    suspend fun markAsCompleted(goalId: Long)
} 