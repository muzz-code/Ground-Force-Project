package com.trapezoidlimited.groundforce.ui

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.ui.dashboard.MissionReportActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class MissionReportActivityTest{

    /** launch the activity**/
    @get :Rule
    val activityRule = ActivityScenarioRule(MissionReportActivity::class.java)



    @Test
    fun test_visibility_of_appbar(){
        onView(withId(R.id.activity_mission_report_ic)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_of_title(){
        onView(withId(R.id.activity_mission_report_intro_tv)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_of_radio_group(){
        onView(withId(R.id.activity_mission_report_group1_rg)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_of_submit_button(){
        onView(withId(R.id.activity_mission_report_nsv)).perform(ViewActions.swipeUp());
        onView(withId(R.id.activity_mission_report_submit_btn)).check(matches(isDisplayed()))
    }

    @Test
    fun test_visibility_of_cancel_button(){
        onView(withId(R.id.activity_mission_report_nsv)).perform(ViewActions.swipeUp());
        onView(withId(R.id.activity_mission_report_cancel_btn)).check(matches(isDisplayed()))
    }

    @Test
    fun test_submit_action(){


        /** instance of Test Function class**/
        val testFunc = MissionTestExt()

        /** launch activity**/
        val activityScenario: ActivityScenario<MissionReportActivity> = ActivityScenario.launch(
            MissionReportActivity::class.java
        )

        activityScenario.moveToState(Lifecycle.State.RESUMED)



        /** fill form**/
        testFunc.fill_form()

        /** find the view and perform a click assertion **/
        onView(withId(R.id.activity_mission_report_submit_btn)).perform(ViewActions.click())


        /**check if toast is visible***/
        onView(withText(R.string.report_saved_str))
            .inRoot(MissionTestExt.ToastMatcher().apply {
                matches(isDisplayed())
            })


    }

}