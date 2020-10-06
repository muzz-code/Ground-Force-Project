package com.trapezoidlimited.groundforce.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.trapezoidlimited.groundforce.R
import kotlinx.android.synthetic.main.fragment_phone_activation.view.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PhoneActivationFragmentTest {
    // Check if fragment is inView
    @Test
    fun test_is_fragment_inView() {
        val scenario = launchFragmentInContainer<PhoneActivationFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.phone_activation_cl))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
    // Test for the right text in enter phone number TextView
    @Test
    fun testEnterPhoneNumberDisplayedFragment() {
        val scenario = launchFragmentInContainer<PhoneActivationFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.phone_activ_enter_phone_no_tv))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.phone_activ_phone_phone_number)))
    }
    // Test for toolbar text display
    @Test
    fun testToolbarTextDisplayedFragment() {
        val scenario = launchFragmentInContainer<PhoneActivationFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.phone_activ_app_bar_tv))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.phone_activ_phone_verification)))
    }
    // Test for the "verify" text display
    @Test
    fun testVerifyTextDisplayedFragment() {
        val scenario = launchFragmentInContainer<PhoneActivationFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.phone_activ_verify_text_tv))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.phone_activ_info_user)))
    }
    // Test for the Terms and Conditions text display
    @Test
    fun testTermsAndConditionsTextDisplayedFragment() {
        val scenario = launchFragmentInContainer<PhoneActivationFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.phone_activ_terms_condition_tv))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.phone_activ_t_and_c)))
    }
    // Check if the button has the right text
    @Test
    fun testContinueButtonTextDisplayedFragment() {
        val scenario = launchFragmentInContainer<PhoneActivationFragment>()
        Espresso.onView(ViewMatchers.withId(R.id.phone_activ_continue_btn))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.phone_activ_continue)))
    }

    // check click on the button
    @Test
    fun validate_button_click(){
        val scenario = launchFragmentInContainer<PhoneActivationFragment>()
        Espresso.onView(withId(R.id.phone_activ_continue_btn)).perform(click())
    }
//  Validate phone number edit text
    @Test
    fun validateEditText_phone_number(){
        val scenario = launchFragmentInContainer<PhoneActivationFragment>()
        Espresso.onView(withId(R.id.phone_activ_phone_no_et))
            .perform(typeText("70323336535"))
            .check(matches(withText("7032333653")))
    }

}