package com.example.fitness_habit_tracker.database

import androidx.room.*
import com.example.fitness_habit_tracker.model.ActivityEntity
import java.time.LocalDateTime

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activities ORDER BY startTime DESC")
    suspend fun getAllActivities(): List<ActivityEntity>

    @Query("SELECT * FROM activities WHERE startTime >= :since ORDER BY startTime DESC")
    suspend fun getActivitiesSince(since: LocalDateTime): List<ActivityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activity: ActivityEntity)

    @Delete
    suspend fun delete(activity: ActivityEntity)
} 