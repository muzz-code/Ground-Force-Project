package com.trapezoidlimited.groundforce.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.DialogInterface
import android.graphics.Color
import android.view.View
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.ui.dialog.VerifiedDialog


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

/**
 * Fade out the view
 */

fun View.crossShow(duration: Long) {


    // Animate the loading view to 0% opacity. After the animation ends,
    // set its visibility to GONE as an optimization step (it won't
    // participate in layout passes, etc.)
    this.animate()
        .alpha(1f)
        .setDuration(duration)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {

                crossfade(duration)
            }
        })
}

/**
 * Fade in the view
 */

fun View.crossfade(duration: Long) {


    // Animate the loading view to 0% opacity. After the animation ends,
    // set its visibility to GONE as an optimization step (it won't
    // participate in layout passes, etc.)
    this.animate()
        .alpha(0f)
        .setDuration(duration)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                crossShow(duration)
            }
        })
}

/**
 * Inflate the dialog fragment that shows location has been verified
 **/
fun Fragment.onLogin() {
    var frag = parentFragmentManager.beginTransaction()
    VerifiedDialog().show(frag, "This")
}

/**
 * Show Dialog
 */
fun Fragment.showAlertDialog(
    message: Int,

    listener: View.OnClickListener? = null
) {
    val dialogBuilder = AlertDialog.Builder(requireActivity())
    dialogBuilder.setMessage(getString(message))
        // if the dialog is cancelable
        .setCancelable(false)
        .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->
            listener

        })

    val alert = dialogBuilder.create()
    alert.setTitle("Test")
    alert.show()
}