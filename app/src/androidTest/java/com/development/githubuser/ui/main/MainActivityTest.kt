package com.development.githubuser.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.development.githubuser.R
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName

class MainActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @BeforeEach
    fun setUp() {
        Thread.sleep(2000)
    }

    @Test
    @DisplayName("Test Navigate to Favorite Activity")
    fun testNavigationToFavoriteActivity() {
        onView(withId(R.id.favoriteMenu)).perform(click())
    }

    @Test
    @DisplayName("Test Navigate to Settings Activity")
    fun testNavigationToSettingsActivity() {
        onView(withId(R.id.settingsMenu)).perform(click())
    }

}