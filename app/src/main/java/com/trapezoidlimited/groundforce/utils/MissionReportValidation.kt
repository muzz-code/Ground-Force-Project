package com.trapezoidlimited.groundforce.utils

import android.widget.RadioGroup
import android.widget.Toast

class MissionReportValidation {

    /**validate full name**/
    fun validateTextInput(str:String): Boolean {
        return str.isNotEmpty()
    }


}