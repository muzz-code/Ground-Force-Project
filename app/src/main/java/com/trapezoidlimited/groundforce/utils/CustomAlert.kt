package com.trapezoidlimited.groundforce.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.trapezoidlimited.groundforce.R

fun Fragment.showDialog(context: Context, titleToDisplay: String?, bodyToDisplay: String?) {
    val dialog = Dialog(context)
    dialog.setContentView(R.layout.verification_result_page)
    dialog.setCancelable(true)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val title = dialog.findViewById<View>(R.id.dialog_title_tv) as TextView
    val body = dialog.findViewById<View>(R.id.dialog_body_tv) as TextView
    title.text = titleToDisplay.toString()
    body.text = bodyToDisplay.toString()
    val dialogButton: Button = dialog.findViewById(R.id.dialog_ok_btn)
    dialogButton.isClickable = true

    dialogButton.setOnClickListener {
        findNavController().navigate(R.id.createPinFragment)
        Toast.makeText(context, "Location verified", Toast.LENGTH_LONG).show()
        dialog.dismiss()
//        requireActivity().finish()
    }
    dialog.show()

}

