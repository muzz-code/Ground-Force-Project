package com.trapezoidlimited.groundforce.validator

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.trapezoidlimited.groundforce.validator.Validation.validateAccountNumber
import com.trapezoidlimited.groundforce.validator.Validation.validateAdditionalPhone
import com.trapezoidlimited.groundforce.validator.Validation.validateAddress
import com.trapezoidlimited.groundforce.validator.Validation.validateEmail
import com.trapezoidlimited.groundforce.validator.Validation.validateName
import com.trapezoidlimited.groundforce.validator.Validation.validateOTP
import com.trapezoidlimited.groundforce.validator.Validation.validatePhoneNumber
import com.trapezoidlimited.groundforce.validator.Validation.validatePin

fun EditText.watchToValidator(
    editFieldType: EditFieldType,
    inputLayer: TextInputLayout? = null,
) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(editText: Editable?) {
            if (editText.toString().trim().isEmpty()) {
                if (inputLayer != null) {
                    error = null
                    inputLayer.error = null
                }
            } else {
                when (editFieldType) {
                    EditFieldType.PHONE -> {
                        if (validatePhoneNumber(editText.toString())) {
                            setValidate()
                        } else {
                            if (editText.toString().length < 10) {
                                setInvalidate("Phone number must be 10 digits")
                            } else {
                                setInvalidate("I")
                            }
                        }
                    }

                    EditFieldType.OTP -> {
                        if (validateOTP(editText.toString())) {
                            setValidate()
                        } else {
                            setInvalidate("OTP must be 4 digits")

                        }
                    }

                    EditFieldType.NAME -> {
                        if (validateName(editText.toString())) {
                            setValidate()
                        } else {
                            setInvalidate("Error: Name too short!")
                        }
                    }

                    EditFieldType.EMAIL -> {
                        if (validateEmail(editText.toString())) {
                            setValidate()
                        } else {
                            setInvalidate("Error: Please provide a valid email address.")
                        }
                    }

                    EditFieldType.ADDRESS -> {
                        if (validateAddress(editText.toString())) {
                            setValidate()
                        } else {
                            setInvalidate("Error: Please provide a valid address.")
                        }
                    }

                    EditFieldType.ADDITIONALPHONE -> {
                        if (editText?.trim().toString().isNotEmpty()) {
                            if (validateAdditionalPhone(editText.toString())) {
                                setValidate()
                            } else {
                                if (editText.toString().length < 11) {
                                    setInvalidate("Error: Phone number must be at least 11 digits")
                                } else {
                                    setInvalidate("Error: Invalid Phone Number")
                                }
                            }
                        }
                    }

                    EditFieldType.ACCOUNTNUMBER -> {
                        if (validateAccountNumber(editText.toString())) {
                            setValidate()
                        } else {
                            if (editText.toString().length < 10) {
                                setInvalidate("Error: Phone number must be at least 11 digits")
                            } else {
                                setInvalidate("Error: Invalid account number")
                            }
                        }
                    }

                    EditFieldType.PIN -> {
                        if (validatePin(editText.toString())) {
                            setValidate()
                        } else {
                            setInvalidate("Error: Pin must be 4 digits")
                        }
                    }
                }
            }
        }


        //When Field is validated
        private fun setValidate() {
            if (inputLayer != null) { //Works for Material Edit text with Input layer
                inputLayer.error = null
            } else { // Works for only Edit Text
                error = null
            }
        }

        //When Field is invalidated
        private fun setInvalidate(message: String) {
            if (inputLayer != null) { //Works for Material Edit text with Input layer
                inputLayer.error = message
            } else { // Works for only Edit Text
                error = message
            }
        }
    })
}