package com.trapezoidlimited.groundforce.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.ui.auth.PhoneVerificationFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PhoneVerificationFragmentTest{
    // Check if fragment is inView
    @Test
    fun test_is_fragment_inView() {
        val scenario = launchFragmentInContainer<PhoneVerificationFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.phone_verification_cl))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // Test for the right text in resend TextView
    @Test
    fun testResendTextDisplayedFragment() {
        val scenario = launchFragmentInContainer<PhoneVerificationFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.phone_verif_resend_tv))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.phone_verif_didnt_get_code_text_str)))
    }

    // Test if phone image is displayed
    @Test
    fun test_is_phone_image_inView() {
        val scenario = launchFragmentInContainer<PhoneVerificationFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.phone_verif_phone_iv))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // Test if otp is displayed
    @Test
    fun test_is_otp_inView() {
        val scenario = launchFragmentInContainer<PhoneVerificationFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.phone_verif_pin_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // Test if confirm button is displayed
    @Test
    fun test_is_confirm_button_inView() {
        val scenario = launchFragmentInContainer<PhoneVerificationFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.phone_verif_confirm_btn))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // Test if confirm text is written on the button
    @Test
    fun testConfirmButtonTextDisplayedFragment() {
        val scenario = launchFragmentInContainer<PhoneVerificationFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.phone_verif_confirm_btn))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.phone_verif_confirm_str)))
    }

    // check click on the button
    @Test
    fun validate_button_click(){
        val scenario = launchFragmentInContainer<PhoneVerificationFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.phone_verif_confirm_btn)).perform(ViewActions.click())
    }
}
