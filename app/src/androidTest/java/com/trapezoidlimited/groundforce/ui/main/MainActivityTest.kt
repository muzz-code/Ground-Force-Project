package com.trapezoidlimited.groundforce.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.trapezoidlimited.groundforce.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest{

    @get: Rule
    val activityRule : ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun test_main_activity_in_view() {
        onView(withId(R.id.activity_main_root_cl)).check(matches(isDisplayed()))
    }

    @Test
    fun check_is_fragment_container_in_view() {
        onView(withId(R.id.activity_main_fcv)).check(matches(isDisplayed()))
    }


}