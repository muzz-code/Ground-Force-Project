package com.trapezoidlimited.groundforce.utils

import android.widget.EditText

class ValidationPhoneNumber {


    companion object {

        /**Vararg to take inputs**/
        operator fun invoke(vararg allEdits: EditText): EditText? {

            for (view in allEdits) {
                if (view.text.isEmpty()) {
                    return view
                }
            }
            return null
        }


        /**validate phone number**/
        fun validatePhoneNumber(num: String): Boolean {
            val nigeriaNumber = Regex("""^(\+?234|0)[897][01]\d{8}${'$'}""")

            return if (!num.matches(nigeriaNumber)) {
                return false
            }
            else if(num.isEmpty()){
                return false
            }
            else {
                true
            }
        }
    }
}