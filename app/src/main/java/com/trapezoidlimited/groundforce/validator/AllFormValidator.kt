package com.trapezoidlimited.groundforce.validator

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText


private lateinit var globalFieldsToWatch: MutableMap<EditText, EditFieldType>
private var globalButtonToEnable: Button? = null
private lateinit var globalValidationArray: MutableList<Boolean>

fun watchAllMyFields(
    fieldsToWatch: MutableMap<EditText, EditFieldType>,
    buttonToEnable: Button? = null,
) {
    globalFieldsToWatch = fieldsToWatch
    globalButtonToEnable = buttonToEnable


    for (field in fieldsToWatch.keys) {
        field.addTextChangedListener(globalWatcher)
    }

}

private val globalWatcher: TextWatcher = object : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        val editTextArray = globalFieldsToWatch.keys.toMutableList()
        val editFieldTypeArray = globalFieldsToWatch.values.toMutableList()

        globalValidationArray = mutableListOf()

        for (i in 0 until globalFieldsToWatch.entries.size) {
            when (editFieldTypeArray[i]) {
                EditFieldType.PHONE -> {
                    val phoneNumber =
                        validatePhoneNumber(editTextArray[i].text.toString())
                    if (phoneNumber) {
                        globalValidationArray.add(true)
                    } else {
                        globalValidationArray.add(false)
                    }
                }
                EditFieldType.OTP -> {
                    val otp = validateOTP(editTextArray[i].text.toString())
                    if (otp) {
                        globalValidationArray.add(true)
                    } else {
                        globalValidationArray.add(false)
                    }
                }

                EditFieldType.NAME -> {
                    val name = validateName(editTextArray[i].text.toString())
                    if (name) {
                        globalValidationArray.add(true)
                    } else {
                        globalValidationArray.add(false)
                    }
                }

                EditFieldType.EMAIL -> {
                    val email = validateEmail(editTextArray[i].text.toString())
                    if (email) {
                        globalValidationArray.add(true)
                    } else {
                        globalValidationArray.add(false)
                    }
                }

                EditFieldType.ADDRESS -> {
                    val address = validateAddress(editTextArray[i].text.toString())
                    if (address) {
                        globalValidationArray.add(true)

                    } else {

                        globalValidationArray.add(false)
                    }

                }

                EditFieldType.ADDITIONALPHONE -> {
                    val phoneNumber = validateAdditionalPhone(editTextArray[i].text.toString())
                    if (phoneNumber) {
                        globalValidationArray.add(true)
                    } else {
                        globalValidationArray.add(false)
                    }
                }

                EditFieldType.ACCOUNTNUMBER -> {
                    val accountNum = validateAccountNumber(editTextArray[i].text.toString())
                    if (accountNum) {
                        globalValidationArray.add(true)
                    } else {
                        globalValidationArray.add(false)
                    }
                }

                EditFieldType.PIN -> {
                    val pin = validatePin(editTextArray[i].text.toString())
                    if (pin) {
                        globalValidationArray.add(true)
                    } else {
                        globalValidationArray.add(false)
                    }
                }
            }

        }


        if (globalButtonToEnable != null && globalValidationArray.contains(false)) {
            globalButtonToEnable!!.isEnabled = false
        } else if (globalButtonToEnable != null && !globalValidationArray.contains(false)) {
            globalButtonToEnable!!.isEnabled = true
        }
    }

}

fun clearFieldsArray() {
    println(globalFieldsToWatch)
    globalFieldsToWatch = mutableMapOf()
    globalValidationArray.clear()
    globalButtonToEnable = null
}

