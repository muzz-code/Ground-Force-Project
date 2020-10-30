package com.trapezoidlimited.groundforce.ui

import android.os.IBinder
import android.view.WindowManager
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Root
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.trapezoidlimited.groundforce.R
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class MissionTestExt {

    class ToastMatcher : TypeSafeMatcher<Root?>() {

        override fun matchesSafely(item: Root?): Boolean {
            val type: Int? = item?.windowLayoutParams?.get()?.type
            if (type == WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW) {
                val windowToken: IBinder = item.decorView.windowToken
                val appToken: IBinder = item.decorView.applicationWindowToken
                if (windowToken === appToken) { // means this window isn't contained by any other windows.
                    return true
                }
            }
            return false
        }

        override fun describeTo(description: Description?) {
            description?.appendText("is toast")
        }
    }




    /** form input details**/

    val landMark = "rock"
    val busStop = "ejigbo"
    val comments = "comments"
    val structure = "structure"

    /** this function fills the form during testing**/
    fun fill_form() {
        val name =
            Espresso.onView(ViewMatchers.withId(R.id.activity_mission_report_landmark_et))
                .perform(ViewActions.replaceText(landMark))
        Espresso.closeSoftKeyboard()

        onView(withId(R.id.activity_mission_report_nsv)).perform(ViewActions.swipeUp());

        Espresso.onView(ViewMatchers.withId(R.id.activity_mission_report_busstop_et))
            .perform(ViewActions.replaceText(busStop))
        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.activity_mission_report_structure_et))
            .perform(ViewActions.replaceText(structure))
        Espresso.closeSoftKeyboard()

        Espresso.onView(ViewMatchers.withId(R.id.activity_mission_report_comments_et))
            .perform(ViewActions.replaceText(comments))
        Espresso.closeSoftKeyboard()
    }
}