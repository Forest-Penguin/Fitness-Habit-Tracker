package com.example.fitness_habit_tracker.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.fitness_habit_tracker.model.Habit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class HabitDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: WorkoutDatabase
    private lateinit var habitDao: HabitDao
    private val testScope = TestScope(UnconfinedTestDispatcher())

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
    fun insertHabit_readsBackCorrectly() = testScope.runTest {
        val habit = Habit(name = "Walking", timesPerWeek = 3, notes = "Morning walks")
        habitDao.insert(habit)

        val allHabits = habitDao.getAllHabits()
        Assert.assertNotNull("Habits list should not be null", allHabits)
        Assert.assertTrue("Habits list should not be empty", allHabits.isNotEmpty())
        
        val firstHabit = allHabits[0]
        Assert.assertEquals("Walking", firstHabit.name)
        Assert.assertEquals(3, firstHabit.timesPerWeek)
        Assert.assertEquals("Morning walks", firstHabit.notes)
    }

    @Test
    fun getHabitsByActivityType_returnsMatchingHabits() = testScope.runTest {
        val walkingHabit = Habit(name = "Walking", timesPerWeek = 3, notes = "Morning walks")
        val runningHabit = Habit(name = "Running", timesPerWeek = 2, notes = "Evening runs")
        
        habitDao.insert(walkingHabit)
        habitDao.insert(runningHabit)

        val walkingHabits = habitDao.getHabitsByActivityType("Walking").first()
        Assert.assertEquals(1, walkingHabits.size)
        Assert.assertEquals("Walking", walkingHabits[0].name)
    }
}
