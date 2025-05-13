package com.example.fitness_habit_tracker.database

import androidx.room.*
import com.example.fitness_habit_tracker.model.Habit
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Insert
    suspend fun insert(habit: Habit)

    @Update
    suspend fun update(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE name = :activityType ORDER BY createdAt DESC")
    fun getHabitsByActivityType(activityType: String): Flow<List<Habit>>
}
