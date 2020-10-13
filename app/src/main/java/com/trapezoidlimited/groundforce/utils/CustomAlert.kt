package com.trapezoidlimited.groundforce.utils

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.trapezoidlimited.groundforce.R

class CustomAlert {


        companion object ShowDialog {
            fun dialog(activity: Activity?, titleToDisplay: String?, bodyToDisplay: String?) {
                val dialog = Dialog(activity!!)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.verification_result_page)
                val title = dialog.findViewById<View>(R.id.dialog_title_tv) as TextView
                val body=dialog.findViewById<View>(R.id.dialog_body_tv) as TextView
                title.text = titleToDisplay.toString()
                body.text=bodyToDisplay.toString()
                val dialogButton: Button = dialog.findViewById<View>(R.id.dialog_ok_btn) as Button
                dialogButton.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
    }