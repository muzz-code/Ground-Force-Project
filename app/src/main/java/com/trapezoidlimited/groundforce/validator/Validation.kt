package com.trapezoidlimited.groundforce.validator

import java.util.regex.Matcher
import java.util.regex.Pattern


//Validate Pin
fun validatePin(pin: String): Boolean {
    return pin.length >= 4
}

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

/** Validating Gender field */
fun validateGender(genderSelected: String): Boolean {
    return genderSelected == "Male" || genderSelected == "Female" || genderSelected == "Others"
}

/** Validating Religion field */
fun validateReligion(religionSelected: String): Boolean {
    return religionSelected == "Christian" || religionSelected == "Muslim" || religionSelected == "Others"
}

/** Validating Email field */
fun validateEmail(email: String): Boolean {
    val pattern: Pattern =
        Pattern.compile("^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-.]+)\\.([a-zA-Z]{2,5})\$");
    val matcher: Matcher = pattern.matcher(email);
    val matchFound = matcher.matches()

    return email.trim().isNotEmpty() && matchFound
}

/** Validating Address field */
fun validateAddress(address: String): Boolean {
    return address.trim().length > 1
}

/** Validating Additional Phone field */
fun validateAdditionalPhone(number: String): Boolean {
    val pattern: Pattern = Pattern.compile("(\\+?234|0)[789][01][0-9]{8}")
    val matcher: Matcher = pattern.matcher(number)
    val matchFound = matcher.matches()
    return !(number.isEmpty() || !matchFound)
}

/** Validating AccountNumber field */
fun validateAccountNumber(number: String): Boolean {
    val pattern: Pattern = Pattern.compile("[0-9]{10}")
    val matcher: Matcher = pattern.matcher(number)
    val matchFound = matcher.matches()
    return !(number.isEmpty() || !matchFound)
}

