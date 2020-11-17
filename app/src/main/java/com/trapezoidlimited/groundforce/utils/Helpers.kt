package com.trapezoidlimited.groundforce.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.trapezoidlimited.groundforce.api.Resource
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
            error?.message?.let { showSnackBar(view, it) }
        }
    }


}