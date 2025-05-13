package com.example.fitness_habit_tracker.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.fitness_habit_tracker.model.Habit
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HabitDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: WorkoutDatabase
    private lateinit var habitDao: HabitDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()

        habitDao = db.habitDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertHabit_readsBackCorrectly() = runBlocking {
        val habit = Habit(name = "Walking", timesPerWeek = 3, notes = "Morning walks")
        habitDao.insert(habit)

        val allHabits = habitDao.getAllHabits().first()

        Assert.assertEquals(1, allHabits.size)
        Assert.assertEquals("Walking", allHabits[0].name)
        Assert.assertEquals(3, allHabits[0].timesPerWeek)
        Assert.assertEquals("Morning walks", allHabits[0].notes)
    }
}
