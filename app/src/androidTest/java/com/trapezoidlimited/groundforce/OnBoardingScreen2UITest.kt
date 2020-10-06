package com.trapezoidlimited.groundforce

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.trapezoidlimited.groundforce.ui.main.OnBoardingScreen2Fragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OnBoardingScreen2UITest {
    @Test
    fun testTitleTextDisplayedFragment() {
        val scenario = launchFragmentInContainer<OnBoardingScreen2Fragment>()
        Espresso.onView(ViewMatchers.withId(R.id.onBoardingScreen2_Fragment_headerText_tv))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.screen1_header_str)))
    }
    @Test
    fun testSubTextDisplayedFragment() {
        /**checks the visibility of the text**/
        val scenario = launchFragmentInContainer<OnBoardingScreen2Fragment>()
        Espresso.onView(ViewMatchers.withId(R.id.onBoardingScreen2_subTitleText_tv))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.screen1_subText_str)))
    }
    @Test
    fun test_is_Fragment_inView() {
        /**In this function we are creating activity inside this test**/
        val scenario = launchFragmentInContainer<OnBoardingScreen2Fragment>()
        Espresso.onView(withId(R.id.OnBoardingScreen2)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}