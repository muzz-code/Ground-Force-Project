package com.trapezoidlimited.groundforce.validator

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner

object AllFormValidator {

    private lateinit var fieldsToWatch: MutableMap<EditText, EditFieldType>
    private var buttonToEnable: Button? = null
    private lateinit var validationArray: MutableList<Boolean>

    fun watchAllMyFields(
        fieldsToWatch: MutableMap<EditText, EditFieldType>,
        buttonToEnable: Button? = null,
    ) {
        this.fieldsToWatch = fieldsToWatch
        this.buttonToEnable = buttonToEnable


        for (field in fieldsToWatch.keys) {
            field.addTextChangedListener(globalWatcher)
        }

    }

    private val globalWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(editable: Editable?) {
            val editTextArray = fieldsToWatch.keys.toMutableList()
            val editFieldTypeArray = fieldsToWatch.values.toMutableList()

            validationArray = mutableListOf()

            for (i in 0 until fieldsToWatch.entries.size) {
                when (editFieldTypeArray[i]) {
                    EditFieldType.PHONE -> {
                        val phoneNumber =
                            FormFieldValidator.validatePhoneNumber(editTextArray[i].text.toString())
                        if (phoneNumber) {
                            validationArray.add(true)
                        } else {
                            validationArray.add(false)
                        }
                    }
                    EditFieldType.OTP -> {
                        val otp = FormFieldValidator.validateOTP(editTextArray[i].text.toString())
                        if (otp) {
                            validationArray.add(true)
                        } else {
                            validationArray.add(false)
                        }
                    }

                    EditFieldType.NAME -> {
                        val name = FormFieldValidator.validateName(editTextArray[i].text.toString())
                        if (name) {
                            validationArray.add(true)
                        } else {
                            validationArray.add(false)
                        }
                    }

                    EditFieldType.EMAIL -> {
                        val email = FormFieldValidator.validateEmail(editTextArray[i].text.toString())
                        if (email) {
                            validationArray.add(true)
                        } else {
                            validationArray.add(false)
                        }
                    }

                    EditFieldType.ADDRESS -> {
                        val address =
                            FormFieldValidator.validateAddress(editTextArray[i].text.toString())
                        if (address) {
                            validationArray.add(true)

                        } else {

                            validationArray.add(false)
                        }

                    }

                    EditFieldType.ADDITIONALPHONE -> {
                        val phoneNumber =
                            FormFieldValidator.validateAdditionalPhone(editTextArray[i].text.toString())
                        if (phoneNumber) {
                            validationArray.add(true)
                        } else {
                            validationArray.add(false)
                        }
                    }

                    EditFieldType.ACCOUNTNUMBER -> {
                        val accountNum = FormFieldValidator.validateAccountNumber(editTextArray[i].text.toString())
                        if (accountNum) {
                            validationArray.add(true)
                        } else {
                            validationArray.add(false)
                        }
                    }
                }

            }


            if (buttonToEnable != null && validationArray.contains(false)) {
                buttonToEnable!!.isEnabled = false
            } else if (buttonToEnable != null && !validationArray.contains(false)) {
                buttonToEnable!!.isEnabled = true
            }
        }

    }

    fun clearFieldsArray() {
        println( fieldsToWatch)
        fieldsToWatch = mutableMapOf()
        validationArray.clear()
        buttonToEnable = null
    }


}