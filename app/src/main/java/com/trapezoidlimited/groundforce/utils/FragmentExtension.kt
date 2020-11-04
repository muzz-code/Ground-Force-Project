package com.trapezoidlimited.groundforce.utils

import android.graphics.Color
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import com.trapezoidlimited.groundforce.R


/**
 * show status bar
 */
fun Fragment.showStatusBar() {
    requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//    activity?.window?.statusBarColor = Color.WHITE;
    requireActivity().window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
}

/**
 * hide status bar
 */

fun Fragment.hideStatusBar(){
    // Hide the status bar.
    activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    activity?.actionBar?.hide()
}


fun Fragment.setVisibility(view: View) {
    view.visibility = View.VISIBLE
}

fun Fragment.setInVisibility(view: View) {
    view.visibility = View.GONE
}

/*
fun Fragment.hideStatusBar(){
    // Enables regular immersive mode.
    // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
    // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
            // Set the content to appear under the system bars so that the
            // content doesn't resize when the system bars hide and show.
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            // Hide the nav bar and status bar
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN)
}
 */