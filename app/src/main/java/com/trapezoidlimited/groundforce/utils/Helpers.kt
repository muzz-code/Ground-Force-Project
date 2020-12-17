package com.trapezoidlimited.groundforce.utils

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.room.RoomViewModel
import com.trapezoidlimited.groundforce.ui.main.MainActivity
import retrofit2.Retrofit
import java.io.File


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

fun Fragment.logOut(roomViewModel: RoomViewModel) {

    fun Fragment.logOut() {
        if (loadFromSharedPreference(requireActivity(), TOKEN).isEmpty()) {
            Intent(requireActivity(), MainActivity::class.java).also {
                saveToSharedPreference(requireActivity(), LOG_OUT, "true")
                roomViewModel.deleteAllMission()
                roomViewModel.deleteAllOngoingMission()
                roomViewModel.deleteAllAgentDetails()
                startActivity(it)
                requireActivity().finish()
            }
        }
    }

    fun agentImageIsSaved(activity: Activity): Boolean {
        val path = File(activity.filesDir, "GroundForce${File.separator}Images")
        val file = File(path, GROUND_FORCE_IMAGE_NAME)
        return file.exists()
    }
}