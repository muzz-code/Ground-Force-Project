package com.trapezoidlimited.groundforce.utils

import android.app.Activity
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Activity.showSnackBar(view: View, message: String) {
    Snackbar.make(
        view,
        message,
        Snackbar.LENGTH_LONG
    ).setAction("Ok") {}.show()
}


fun Activity.showToast(message: String) {
    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
}
