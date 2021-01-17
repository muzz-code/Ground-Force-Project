package com.trapezoidlimited.groundforce.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.ui.dialog.FailedDialog
import com.trapezoidlimited.groundforce.ui.dialog.VerifiedDialog
import com.trapezoidlimited.groundforce.ui.dialog.WelcomeDialog
import retrofit2.http.Url
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL


/**
 * show status bar
 */
fun Fragment.showStatusBar() {
    requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//    activity?.window?.statusBarColor = Color.WHITE;
    requireActivity().window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
}

fun Activity.showSnackBar(message: String) {
    Snackbar.make(findViewById(android.R.id.content), message,
    Snackbar.LENGTH_LONG
    ).setAction("Ok") {}.show()
}


/**
 * hide status bar
 */

@RequiresApi(Build.VERSION_CODES.O)
fun Fragment.hideStatusBar() {
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
fun Fragment.setSuccessDialog() {
    val frag = parentFragmentManager.beginTransaction()
    VerifiedDialog().show(frag, "This")
}

/**
 * Inflate the welcome fragment that shows welcome to users
 **/

fun Fragment.showWelcomeDialog() {
    val fragment = parentFragmentManager.beginTransaction()
    WelcomeDialog().show(fragment, "Welcome")
}

/**
 * Inflate the welcome fragment that shows welcome to users
 **/

fun Fragment.showFailedDialog() {
    val fragment = parentFragmentManager.beginTransaction()
    FailedDialog().show(fragment, "Failed")
}

/**
 * Show Dialog
 */
fun Fragment.showAlertDialog(
    message: String,
    title: String,
    dialogInterface: DialogInterface.OnClickListener
) {
    val dialogBuilder = AlertDialog.Builder(requireActivity())
    dialogBuilder.setMessage(message)
        .setPositiveButton("Yes", dialogInterface)
        .setNegativeButton("Cancel", null)

    val alert = dialogBuilder.create()
    alert.setTitle(title)
    alert.show()
}

fun Activity.showAlertDialog(
    message: String,
    title: String,
    dialogInterface: DialogInterface.OnClickListener
) {
    val dialogBuilder = AlertDialog.Builder(this)
    dialogBuilder.setMessage(message)
        .setPositiveButton("Yes", dialogInterface)
        .setNegativeButton("Cancel", null)

    val alert = dialogBuilder.create()
    alert.setTitle(title)
    alert.show()
}

fun showSnackBar(view: View, message: String) {
    Snackbar.make(
        view,
        message,
        Snackbar.LENGTH_LONG
    ).setAction("Ok") {}.show()
}


fun Fragment.uriToBitmap(uriImage: Uri): Bitmap? {
    var mBitmap: Bitmap? = null
    if (Build.VERSION.SDK_INT < 28) {
        mBitmap = MediaStore.Images.Media.getBitmap(
            requireActivity().contentResolver,
            uriImage
        )
    } else {
        val source = ImageDecoder.createSource(requireActivity().contentResolver, uriImage)
        mBitmap = ImageDecoder.decodeBitmap(source)
    }
    return mBitmap
}

fun Fragment.saveBitmap(bmp: Bitmap?): File? {
    val extStorageDirectory = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    var outStream: OutputStream? = null
    var file: File? = null
    val time = System.currentTimeMillis()
    val child = "JPEG_${time}_.jpg"
    // String temp = null;
    if (extStorageDirectory != null) {
        file = File(extStorageDirectory, child)
        if (file.exists()) {
            file.delete()
            file = File(extStorageDirectory, child)
        }
        try {
            outStream = FileOutputStream(file)
            bmp?.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            outStream.flush()
            outStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
    return file
}
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

