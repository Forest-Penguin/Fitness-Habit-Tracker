package com.example.fitness_habit_tracker.ui

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import com.example.fitness_habit_tracker.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GoalsActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(GoalsActivity::class.java)

    @Test
    fun walkingGoalInputIsDisplayed() {
        onView(withId(R.id.tilWalkingGoal)).check(matches(isDisplayed()))
    }
}
