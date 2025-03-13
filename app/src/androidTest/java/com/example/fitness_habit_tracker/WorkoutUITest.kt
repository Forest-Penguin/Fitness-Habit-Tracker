package com.example.fitness_habit_tracker

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.fitness_habit_tracker.MainActivity
import org.junit.Rule
import org.junit.Test

class WorkoutUITest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testStartWorkoutButton() {
        // Click on the button
        onView(withId(R.id.startWorkoutButton)).perform(click())

        // Check if the workout screen is displayed
      //  onView(withId(R.id.workoutScreen)).check(matches(isDisplayed()))
    }
}
