package com.trapezoidlimited.groundforce

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.trapezoidlimited.groundforce.ui.onboarding.OnBoardingScreen4Fragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OnBoardingScreen4UITest {
    @Test
    fun testTitleTextDisplayedOnFragment() {
        /**
         * Checks if the Title text is displayed on the fragment.
         **/
        val scenario = launchFragmentInContainer<OnBoardingScreen4Fragment>()
        onView(withId(R.id.onBoardingScreen4_Fragment_headerText_tv))
            .check(matches(withText(R.string.screen_4_text1)))
    }
    @Test
    fun testSubTextDisplayedOnFragment() {
        /**
         * Checks if the subtitle text is displayed on the fragment.
         **/
        val scenario = launchFragmentInContainer<OnBoardingScreen4Fragment>()
        onView(withId(R.id.onBoardingScreen4_subTitleText_tv))
            .check(matches(withText(R.string.screen_4_text2)))
    }
    @Test
    fun testIfFragmentInView() {
        /**
         * This is to test if the fragment itself is displayed using its ID
         **/
        val scenario = launchFragmentInContainer<OnBoardingScreen4Fragment>()
        onView(withId(R.id.OnBoardingScreen4)).check(matches(ViewMatchers.isDisplayed()))
    }
}