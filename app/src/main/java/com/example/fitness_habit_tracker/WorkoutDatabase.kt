package com.example.fitness_habit_tracker

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

// Motion Data Entity (Represents Table in Database)
@Entity(tableName = "motion_data")
data class MotionData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val x: Float,
    val y: Float,
    val z: Float
)

// Data Access Object (DAO)
@Dao
interface MotionDataDao {
    @Insert
    suspend fun insertData(data: MotionData)

    @Query("SELECT * FROM motion_data ORDER BY timestamp DESC LIMIT 100")
    suspend fun getRecentData(): List<MotionData>
}

// Room Database
@Database(entities = [MotionData::class], version = 1, exportSchema = false)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun motionDataDao(): MotionDataDao
}

