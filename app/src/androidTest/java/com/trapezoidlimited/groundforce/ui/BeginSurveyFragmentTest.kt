package com.trapezoidlimited.groundforce.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.ui.dashboard.survey.BeginSurveyFragment
import org.junit.Test
import org.junit.runner.RunWith

//@RunWith(AndroidJUnit4::class)
//class PhoneVerificationFragmentTest{
//    // Check if fragment is inView
//    @Test
//    fun test_is_fragment_inView() {
//        val scenario = launchFragmentInContainer<PhoneVerificationFragment>()
//        Espresso.onView(ViewMatchers.withId(R.id.phone_verification_cl))
//            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//    }

@RunWith(AndroidJUnit4::class)
class BeginSurveyFragmentTest{


    //check if fragment is in view

    @Test
    fun isFragmentInView(){
        val scenario = launchFragmentInContainer<BeginSurveyFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.begin_survey_fragment_root))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}