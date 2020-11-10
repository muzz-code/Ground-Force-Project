package com.trapezoidlimited.groundforce.validator

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.watchToValidator(
    editFieldType: EditFieldType
) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(editText: Editable?) {
            if (editText.toString().trim().isEmpty()) {
                error = null
            } else {
                when (editFieldType) {
                    EditFieldType.PHONE -> {
                        error = if (validatePhoneNumber(editText.toString())) {
                            null
                        } else {
                            if (editText.toString().length < 10) {
                                "Phone number must be 10 digits"
                            } else {
                                "Invalid phone number"
                            }
                        }
                    }

                    EditFieldType.OTP -> {
                        error = if (validateOTP(editText.toString())) {
                            null
                        } else {
                            "OTP must be 4 digits"
                        }
                    }

                    EditFieldType.NAME -> {
                        error = if (validateName(editText.toString())) {
                            null
                        } else {
                            "Name must be more than one character long."
                        }
                    }

                    EditFieldType.EMAIL -> {
                        error = if (validateEmail(editText.toString())) {
                            null
                        } else {
                            "Please provide a valid email address."
                        }
                    }

                    EditFieldType.ADDRESS -> {
                        error = if (validateAddress(editText.toString())) {
                            null
                        } else {
                            "Please provide a valid address."
                        }
                    }

                    EditFieldType.ADDITIONALPHONE -> {
                        if (editText?.trim().toString().isNotEmpty()) {
                            error = if (validateAdditionalPhone(editText.toString())) {
                                null
                            } else {
                                if (editText.toString().length < 11) {
                                    "Phone number must be at least 11 digits"
                                } else {
                                    "Invalid phone number"
                                }
                            }
                        }
                    }

                    EditFieldType.ACCOUNTNUMBER -> {
                        error = if (validateAccountNumber(editText.toString())) {
                            null
                        } else {
                            if (editText.toString().length < 10) {
                                "Account number must be 10 digits"
                            } else {
                                "Invalid account number"
                            }
                        }
                    }

                    EditFieldType.PIN -> {
                        error = if (validatePin(editText.toString())) {
                            null
                        } else {
                            "Phone number must be 4 digits"
                        }
                    }
                }
            }
        }
    })
}