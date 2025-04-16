package com.example.fitness_habit_tracker

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


// Motion Data Entity
@Entity(tableName = "motion_data")
data class MotionData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val x: Float,
    val y: Float,
    val z: Float,
    val activityType: String // <-- added
)

// DAO
@Dao
interface MotionDataDao {
    @Insert
    fun insertData(data: MotionData)

    @Query("SELECT * FROM motion_data ORDER BY timestamp DESC LIMIT 100")
    fun getRecentData(): List<MotionData>
}

// Room Database
@Database(entities = [MotionData::class], version = 2, exportSchema = false)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun motionDataDao(): MotionDataDao

    companion object {
        @Volatile private var INSTANCE: WorkoutDatabase? = null

        fun getInstance(context: Context): WorkoutDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDatabase::class.java,
                    "workout_db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE motion_data ADD COLUMN activityType TEXT NOT NULL DEFAULT 'UNKNOWN'")
            }
        }
    }
}
