package com.trapezoidlimited.groundforce.validator

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.regex.Matcher
import java.util.regex.Pattern

object FormFieldValidator {
    /** Validating phone number field */
    fun validatePhoneNumber(number: String): Boolean {
        val pattern: Pattern = Pattern.compile("[789][01][0-9]{8}")
        val matcher: Matcher = pattern.matcher(number)
        val matchFound = matcher.matches()
        return !(number.isEmpty() || !matchFound)
    }

    /** Validating OTP field */
    fun validateOTP(string: String): Boolean {
        return string.isNotEmpty() && string.length == 4
    }

    /** Validating Name field */
    fun validateName(name: String): Boolean {
        return name.trim().length > 1
    }

    /** Validating DateOfBirth field */
    fun validateDateOfBirth(date: String): Boolean {
        return date.trim().isNotEmpty()
    }

    fun validateGender(genderSelected: String): Boolean {
        return genderSelected == "Male" || genderSelected == "Female" || genderSelected == "Others"
    }

    fun validateReligion(religionSelected: String): Boolean {
        return religionSelected == "Christian" || religionSelected == "Muslim" || religionSelected == "Others"
    }

    fun validateEmail(email: String): Boolean {
        val pattern: Pattern =
            Pattern.compile("^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-.]+)\\.([a-zA-Z]{2,5})\$");
        val matcher: Matcher = pattern.matcher(email);
        val matchFound = matcher.matches()

        return email.trim().isNotEmpty() && matchFound
    }

    fun validateAddress(address: String): Boolean {
        return address.trim().length > 1
    }

    fun validateAdditionalPhone(number: String): Boolean {
        val pattern: Pattern = Pattern.compile("(\\+?234|0)[789][01][0-9]{8}")
        val matcher: Matcher = pattern.matcher(number)
        val matchFound = matcher.matches()
        return !(number.isEmpty() || !matchFound)
    }

    fun validateAccountNumber(number: String): Boolean {
        val pattern: Pattern = Pattern.compile("[0-9]{10}")
        val matcher: Matcher = pattern.matcher(number)
        val matchFound = matcher.matches()
        return !(number.isEmpty() || !matchFound)
    }

    /**
     * Extension Function to Watch and Validate Individual Input Fields
     */
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
                                "Name must be at least one character long."
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
                                    "Phone number must be 10 digits"
                                } else {
                                    "Invalid account number"
                                }
                            }

                        }

                    }
                }
            }

        })
    }
}