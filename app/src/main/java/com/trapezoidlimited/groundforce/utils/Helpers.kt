package com.trapezoidlimited.groundforce.utils

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.ui.auth.LoginFragment
import retrofit2.Retrofit
import javax.inject.Inject


fun Fragment.handleApiError(
    failure: Resource.Failure,
    retrofit: Retrofit,
    view: View
){
    val errorUtils = ErrorUtils(retrofit)

    when{
        failure.isNetworkError -> {
            showSnackBar(view, "Please confirm network connection" )
        }

        else -> {
            val error = failure.errorBody?.let { it1 -> errorUtils.parseError(it1) }
            error?.message?.let { showSnackBar(view, it) }
        }
    }


}

//Hide and Show Progress Bars
fun ProgressBar.hide(button: Button? = null) {
    visibility = View.GONE
    if (button != null) {
        button.isEnabled = true
    }
}
fun ProgressBar.show(button: Button? = null) {
    visibility = View.VISIBLE
    if (button != null) {
        button.isEnabled = false
    }
}