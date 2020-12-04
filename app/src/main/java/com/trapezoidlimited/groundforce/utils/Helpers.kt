package com.trapezoidlimited.groundforce.utils

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.ui.main.MainActivity
import retrofit2.Retrofit


fun Fragment.handleApiError(
    failure: Resource.Failure,
    retrofit: Retrofit,
    view: View
) {
    val errorUtils = ErrorUtils(retrofit)

    when {
        failure.isNetworkError -> {
            showSnackBar(view, "Please confirm network connection")
        }

        else -> {
            val error = failure.errorBody?.let { it1 -> errorUtils.parseError(it1) }
            error?.errors?.let { showSnackBar(view, it.message!!) }
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

fun Fragment.logOut() {
    if (loadFromSharedPreference(requireActivity(), TOKEN).isEmpty()) {
        Intent(requireActivity(), MainActivity::class.java).also {
            saveToSharedPreference(requireActivity(), LOG_OUT, "true")
            startActivity(it)
            requireActivity().finish()
        }
    }
}