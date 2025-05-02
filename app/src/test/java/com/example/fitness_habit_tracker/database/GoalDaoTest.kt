package com.example.fitness_habit_tracker.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.fitness_habit_tracker.model.Goal
import com.example.fitness_habit_tracker.util.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import java.time.LocalDateTime


@RunWith(AndroidJUnit4::class)
class GoalDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: WorkoutDatabase
    private lateinit var goalDao: GoalDao

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()
        goalDao = db.goalDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertGoal_readGoal_goalInserted() = runTest {
        val goal = Goal(
            activityType = "Running",
            targetValue = 1000f,
            unit = "steps",
            endDate = LocalDateTime.now().plusDays(1)
        )

        goalDao.insert(goal)
        val result = goalDao.getAllGoals().getOrAwaitValue()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Running", result[0].activityType)
    }

    @Test
    fun updateProgress_setsCorrectValue() = runTest {
        val goal = Goal(
            activityType = "Running",
            targetValue = 1000f,
            currentValue = 0f,
            unit = "steps",
            endDate = LocalDateTime.now().plusDays(1)
        )

        goalDao.insert(goal)
        val inserted = goalDao.getAllGoals().getOrAwaitValue().first()

        goalDao.updateProgress(inserted.id, 500f)
        val updated = goalDao.getAllGoals().getOrAwaitValue().first()

        Assert.assertEquals(500f, updated.currentValue, 0.001f)
    }

    @Test
    fun markAsCompleted_setsCompletedToTrue() = runTest {
        val goal = Goal(
            activityType = "Running",
            targetValue = 1000f,
            completed = false,
            unit = "steps",
            endDate = LocalDateTime.now().plusDays(1)
        )

        goalDao.insert(goal)
        val inserted = goalDao.getAllGoals().getOrAwaitValue().first()

        goalDao.markAsCompleted(inserted.id)
        val updated = goalDao.getAllGoals().getOrAwaitValue().first()

        Assert.assertTrue(updated.completed)
    }

    @Test
    fun getCompletedGoals_returnsOnlyCompletedGoals() = runTest {
        val completedGoal = Goal(
            activityType = "Walking",
            targetValue = 500f,
            unit = "steps",
            completed = true,
            endDate = LocalDateTime.now().plusDays(2)
        )

        val incompleteGoal = Goal(
            activityType = "Running",
            targetValue = 1000f,
            unit = "steps",
            completed = false,
            endDate = LocalDateTime.now().plusDays(2)
        )

        goalDao.insert(completedGoal)
        goalDao.insert(incompleteGoal)

        val completed = goalDao.getCompletedGoals().getOrAwaitValue()
        Assert.assertEquals(1, completed.size)
        Assert.assertTrue(completed[0].completed)
    }

    @Test
    fun getActiveGoals_returnsOnlyActiveUnexpiredGoals() = runTest {
        val activeGoal = Goal(
            activityType = "Yoga",
            targetValue = 30f,
            unit = "min",
            completed = false,
            endDate = LocalDateTime.now().plusDays(3)
        )

        val expiredGoal = Goal(
            activityType = "Yoga",
            targetValue = 30f,
            unit = "min",
            completed = false,
            endDate = LocalDateTime.now().minusDays(1)
        )

        val completedGoal = Goal(
            activityType = "Yoga",
            targetValue = 30f,
            unit = "min",
            completed = true,
            endDate = LocalDateTime.now().plusDays(3)
        )

        goalDao.insert(activeGoal)
        goalDao.insert(expiredGoal)
        goalDao.insert(completedGoal)

        val active = goalDao.getActiveGoals().getOrAwaitValue()
        Assert.assertEquals(1, active.size)
        Assert.assertFalse(active[0].completed)
        Assert.assertTrue(active[0].endDate.isAfter(LocalDateTime.now()))
    }

    @Test
    fun getGoalsByActivityType_returnsMatchingGoals() = runTest {
        val runningGoal = Goal(
            activityType = "Running",
            targetValue = 1000f,
            unit = "steps",
            completed = false,
            endDate = LocalDateTime.now().plusDays(1)
        )

        val walkingGoal = Goal(
            activityType = "Walking",
            targetValue = 500f,
            unit = "steps",
            completed = false,
            endDate = LocalDateTime.now().plusDays(1)
        )

        goalDao.insert(runningGoal)
        goalDao.insert(walkingGoal)

        val result = goalDao.getGoalsByActivityType("Running").getOrAwaitValue()
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Running", result[0].activityType)
    }
}
