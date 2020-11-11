package com.trapezoidlimited.groundforce.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.ui.auth.LoginFragment


fun Fragment.handleApiError(
    failure: Resource.Failure
){

    when{
        failure.isNetworkError -> Snackbar.make(requireView(), "Please confirm network connection", Snackbar.LENGTH_LONG)
        failure.errorCode?.toInt() == 400 -> {
            if (this is LoginFragment) {
                Snackbar.make(requireView(), "Incorrect password or email.", Snackbar.LENGTH_LONG)
            }
        }
        else -> {
            val error = failure.errorBody?.message().toString()
            Snackbar.make(requireView(), error, Snackbar.LENGTH_LONG)
        }
    }


}


fun showSnackBar(view: View, message: String) {
    Snackbar.make(
        view,
        message,
        Snackbar.LENGTH_LONG
    ).setAction("Ok") {}.show()
}
