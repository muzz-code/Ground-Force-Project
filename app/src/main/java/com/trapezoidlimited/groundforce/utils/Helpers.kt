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
import com.trapezoidlimited.groundforce.api.Resource
import com.trapezoidlimited.groundforce.room.RoomViewModel
import com.trapezoidlimited.groundforce.ui.main.MainActivity
import retrofit2.Retrofit
import java.io.File
import java.lang.Exception
import java.lang.StringBuilder


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
            showSnackBar(view, "Connection time out or bad network connection. Try again")
        }

        else -> {
            try {

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

                    println(errorMessage)

                    if (errorMessage != null) {

                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT)
                            .show()

                       // showSnackBar(requireView(), errorMessage)


                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Something went wrong!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                }

            } catch (e: Exception) {
                showSnackBar(requireView(), "Bad request. Check input again.")
            }


        }
    }
}

fun Fragment.handleApiError(
    failure: Resource.Failure,
    retrofit: Retrofit,
    view: View,
    message: String = "",
    navDestinationId: Int = 0,
    data: String? = null,
    dataType: String? = null
) {
    val errorUtils = ErrorUtils(retrofit)

    when {
        failure.isNetworkError -> {
            showSnackBar(view, "Connection time out or bad network connection. Try again")
        }

        else -> {
            try {

                val error = failure.errorBody?.let { it1 -> errorUtils.parseError(it1) }

                val errorMessage = error?.errors?.message

                println("RUNNING")

                if (errorMessage == message) {

                    println("RUNS")
                    /** Saving EMAIL in sharedPreference*/
                    if (data != null) {
                        if (dataType != null) {
                            saveToSharedPreference(requireActivity(), dataType, data)
                        }
                    }

                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(navDestinationId)


                } else {
                    //error?.errors?.let { showSnackBar(view, it.message!!) }

                        println(errorMessage)

                    if (errorMessage != null) {
                        showSnackBar(requireView(), errorMessage)
                    }
                }

            } catch (e: Exception) {
                showSnackBar(requireView(), "Bad request. Check input again.")
            }

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
            showSnackBar(view, "Connection time out or bad network connection. Try again")
        }

        else -> {
            try {
                val error = failure.errorBody?.let { it1 -> errorUtils.parseError(it1) }
                error?.errors?.let { showSnackBar(view, it.message!!) }
            } catch (e: Exception) {
                Toast.makeText(this, "Bad request. Check input again.", Toast.LENGTH_SHORT).show()
            }
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



    fun agentImageIsSaved(activity: Activity): Boolean {
        val path = File(activity.filesDir, "GroundForce${File.separator}Images")
        val file = File(path, GROUND_FORCE_IMAGE_NAME)
        return file.exists()
    }

}

fun Fragment.splitDate(date: String): String {
    val splittedDate = date.split("-")
    val year = splittedDate[0]
    val month = splittedDate[1]
    val day = splittedDate[2].substring(0, 2)
    var monthInWord = ""

    val stringBuilder = StringBuilder()

    when (month) {
        "01" -> {
            monthInWord = "Jan"
        }
        "02" -> {
            monthInWord = "Feb"
        }
        "03" -> {
            monthInWord = "Mar"
        }
        "04" -> {
            monthInWord = "Apr"

        }
        "05" -> {
            monthInWord = "May"

        }
        "06" -> {
            monthInWord = "Jun"

        }
        "07" -> {
            monthInWord = "Jul"

        }
        "08" -> {
            monthInWord = "Aug"

        }
        "09" -> {
            monthInWord = "Sept"

        }
        "10" -> {
            monthInWord = "Oct"

        }
        "11" -> {
            monthInWord = "Nov"

        }
        "12" -> {
            monthInWord = "Dec"

        }

    }

    return stringBuilder.append(day).append(" ").append(monthInWord).append(",").append(" ")
        .append(year).toString()

}


fun Fragment.formattingDateToMMDDYYYY(date: String): String{
    val dateList = date.filter { it != ',' }.split(" ")

    println(dateList)

    val month = dateList[0]
    val day = dateList[1]
    val year = dateList[2]
    var monthInDigit = ""

    val dateBuilder = StringBuilder()

    when(month) {
        "Jan" -> {
            monthInDigit = "01"
        }
        "Feb" -> {
            monthInDigit = "02"
        }
        "Mar" -> {
            monthInDigit = "03"
        }
        "Apr" -> {
            monthInDigit = "04"
        }

        "May" -> {
            monthInDigit = "05"
        }

        "Jun" -> {
            monthInDigit = "06"
        }

        "Jul" -> {
            monthInDigit = "07"
        }

        "Aug" -> {
            monthInDigit = "08"
        }

        "Sep" -> {
            monthInDigit = "09"
        }

        "Oct" -> {
            monthInDigit = "10"
        }

        "Nov" -> {
            monthInDigit = "11"
        }

        "Dec" -> {
            monthInDigit = "12"
        }

    }

    return dateBuilder.append(monthInDigit).append("/").append(day).append("/").append(year).toString()
}

fun Fragment.formattingDateToMMDDYYYYV10(date: String): String{
    val dateList = date.filter { it != ',' }.split(" ")

    println(dateList)

    //DD/MM/YYYY

    val month = dateList[1]
    val day = dateList[0]
    val year = dateList[2]
    var monthInDigit = ""

    val dateBuilder = StringBuilder()

    when(month) {
        "Jan" -> {
            monthInDigit = "01"
        }
        "Feb" -> {
            monthInDigit = "02"
        }
        "Mar" -> {
            monthInDigit = "03"
        }
        "Apr" -> {
            monthInDigit = "04"
        }

        "May" -> {
            monthInDigit = "05"
        }

        "Jun" -> {
            monthInDigit = "06"
        }

        "Jul" -> {
            monthInDigit = "07"
        }

        "Aug" -> {
            monthInDigit = "08"
        }

        "Sep" -> {
            monthInDigit = "09"
        }

        "Oct" -> {
            monthInDigit = "10"
        }

        "Nov" -> {
            monthInDigit = "11"
        }

        "Dec" -> {
            monthInDigit = "12"
        }

    }

    return dateBuilder.append(monthInDigit).append("/").append(day).append("/").append(year).toString()
}