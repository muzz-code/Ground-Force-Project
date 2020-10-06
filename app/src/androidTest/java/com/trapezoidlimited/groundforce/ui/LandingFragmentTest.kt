package com.trapezoidlimited.groundforce.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.trapezoidlimited.groundforce.R
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LandingFragmentTest {
    // Check if fragment is inView
    @Test
    fun test_is_fragment_inView() {
        val scenario = launchFragmentInContainer<LandingFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.landing_cl))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // Check if background image is visible
    @Test
    fun test_is_background_image_inView() {
        val scenario = launchFragmentInContainer<LandingFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.landing_man_on_street2_iv))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // Check if welcome text is visible
    @Test
    fun testWelcomeTextDisplayedFragment() {
        val scenario = launchFragmentInContainer<LandingFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.landing_welcome_tv))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.landing_welcome)))
    }

    // Check if sub title text is visible
    @Test
    fun testSubTitleTextDisplayedFragment() {
        val scenario = launchFragmentInContainer<LandingFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.landing_sub_title_tv))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.landing_sub_welcome)))
    }

    // Check if create account button is visible
    @Test
    fun test_is_create_account_button_inView() {
        val scenario = launchFragmentInContainer<LandingFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.landing_create_acc_btn))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // Check if sign up button is visible
    @Test
    fun test_is_sign_up_button_inView() {
        val scenario = launchFragmentInContainer<LandingFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.landing_sign_up_btn))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // Check if sign up with google button is visible
    @Test
    fun test_is_sign_up_with_google_button_inView() {
        val scenario = launchFragmentInContainer<LandingFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.landing_sign_up_google_btn))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // Check if create account button text is displayed
    @Test
    fun testCreateAccountButtonTextDisplayedFragment() {
        val scenario = launchFragmentInContainer<LandingFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.landing_create_acc_btn))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.landing_create_account)))
    }

    // Check if sign up button text is displayed
    @Test
    fun testSignUpButtonTextDisplayedFragment() {
        val scenario = launchFragmentInContainer<LandingFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.landing_sign_up_btn))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.landing_sign_up)))
    }

    // Check if sign up with google button text is displayed
    @Test
    fun testSignUpWithGoogleButtonTextDisplayedFragment() {
        val scenario = launchFragmentInContainer<LandingFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.landing_sign_up_google_btn))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.landing_sign_up_with_google)))
    }

}