package com.trapezoidlimited.groundforce.utils

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.room.RoomViewModel
import com.trapezoidlimited.groundforce.ui.main.MainActivity
import retrofit2.Retrofit
import java.lang.Exception


fun Fragment.handleApiError(
    roomViewModel: RoomViewModel,
    activity: Activity,
    failure: Resource.Failure,
    retrofit: Retrofit,
    view: View,
    message: String = "",
    navDestinationId: Int = 0
) {
    val errorUtils = ErrorUtils(retrofit)

    when {
        failure.isNetworkError -> {
            showSnackBar(view, "Please confirm network connection")
        }

        else -> {
            val error = failure.errorBody?.let { it1 -> errorUtils.parseError(it1) }

            val errorMessage = error?.errors?.message

            if (failure.errorCode == UNAUTHORIZED) {

                logOut(roomViewModel, activity)
            }

            if (errorMessage == message) {

                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(navDestinationId)

            } else {
                //error?.errors?.let { showSnackBar(view, it.message!!) }
                if (errorMessage != null) {
                    showSnackBar(requireView(), errorMessage)
                } else {
                    Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT)
                        .show()
                }

            }

            //Log.i("ERROR", "$errorMessage")

        }
    }
}

fun Fragment.handleApiError(
    failure: Resource.Failure,
    retrofit: Retrofit,
    view: View,
    message: String = "",
    navDestinationId: Int = 0
) {
    val errorUtils = ErrorUtils(retrofit)

    when {
        failure.isNetworkError -> {
            showSnackBar(view, "Please confirm network connection")
        }

        else -> {
            val error = failure.errorBody?.let { it1 -> errorUtils.parseError(it1) }

            val errorMessage = error?.errors?.message

            if (errorMessage == message) {

                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(navDestinationId)

            } else {
                //error?.errors?.let { showSnackBar(view, it.message!!) }
                if (errorMessage != null) {
                    showSnackBar(requireView(), errorMessage)
                }
            }

            //Log.i("ERROR", "$errorMessage")

        }
    }
}


fun Activity.handleApiError(
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

fun Fragment.logOut(roomViewModel: RoomViewModel, activity: Activity) {

    Intent(activity, MainActivity::class.java).also {
        saveToSharedPreference(activity, TOKEN, "")
        saveToSharedPreference(activity, LOG_OUT, "true")
        roomViewModel.deleteAllMission()
        roomViewModel.deleteAllOngoingMission()
        roomViewModel.deleteAllAgentDetails()
        startActivity(it)
        requireActivity().finish()
    }

}