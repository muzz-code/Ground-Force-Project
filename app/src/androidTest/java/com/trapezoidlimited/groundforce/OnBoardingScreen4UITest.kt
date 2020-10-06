package com.trapezoidlimited.groundforce

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.trapezoidlimited.groundforce.ui.main.OnBoardingScreen4Fragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OnBoardingScreen4UITest {
    @Test
    fun testTitleTextDisplayedFragment() {
        val scenario = launchFragmentInContainer<OnBoardingScreen4Fragment>()
        onView(withId(R.id.onBoardingScreen4_Fragment_headerText_tv))
            .check(matches(withText(R.string.screen_4_text1)))
    }
    @Test
    fun testSubTextDisplayedFragment() {
        /**checks the visibility of the text**/
        val scenario = launchFragmentInContainer<OnBoardingScreen4Fragment>()
        onView(withId(R.id.onBoardingScreen4_subTitleText_tv))
            .check(matches(withText(R.string.screen_4_text2)))
    }
    @Test
    fun test_is_Fragment_inView() {
        /**In this function we are creating activity inside this test**/
        val scenario = launchFragmentInContainer<OnBoardingScreen4Fragment>()
        onView(withId(R.id.OnBoardingScreen4)).check(matches(ViewMatchers.isDisplayed()))
    }
}