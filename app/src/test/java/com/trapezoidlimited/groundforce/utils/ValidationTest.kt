package com.trapezoidlimited.groundforce.utils
import com.trapezoidlimited.groundforce.validator.Validation.validateAccountNumber
import com.trapezoidlimited.groundforce.validator.Validation.validateAddress
import com.trapezoidlimited.groundforce.validator.Validation.validateDateOfBirth
import com.trapezoidlimited.groundforce.validator.Validation.validateEmail
import com.trapezoidlimited.groundforce.validator.Validation.validateGender
import com.trapezoidlimited.groundforce.validator.Validation.validateName
import com.trapezoidlimited.groundforce.validator.Validation.validateOTP
import com.trapezoidlimited.groundforce.validator.Validation.validatePhoneNumber
import com.trapezoidlimited.groundforce.validator.Validation.validateReligion
import org.junit.Test

import org.junit.Assert.*


class ValidationTest {


    /*** email validation ***/
    @Test
    fun `email is an empty string return false` () {
        assertEquals(validateEmail(""), false)
    }

    @Test
    fun `email has a wrong email format return false` () {
        assertEquals(validateEmail("abcgmail.com"), false)
    }

    @Test
    fun `email has a correct email format return true` () {
        assertEquals(validateEmail("abc@gmail.com"), true)
    }

    /*** pin validation **/
    @Test
    fun `pin is an empty string return false` () {
        assertEquals(validateOTP(""), false)
    }
    @Test
    fun `pin is less than four digits return false` () {
        assertEquals(validateOTP("123"), false)
    }
    @Test
    fun `pin has four digits return true` () {
        assertEquals(validateOTP("1234"), true)
    }

    /*** phone number validation **/
    @Test
    fun `phone number field is empty return false` () {
        assertEquals(validatePhoneNumber(""), false)
    }

    @Test
    fun `phone number less that ten digit return false` () {
        assertEquals(validatePhoneNumber("8045678"), false)
    }

    @Test
    fun `phone number with valid 10 digits return true` () {
        assertEquals(validatePhoneNumber("8044678934"), true)
    }

    /*** name field validation **/
    @Test
    fun `name field is empty return false` () {
        assertEquals(validateName(""), false)
    }

    @Test
    fun `name field has at least two characters return true` () {
        assertEquals(validateName("ab"), true)
    }

    /*** name field validation **/
    @Test
    fun `date of birth field is empty return false` () {
        assertEquals(validateDateOfBirth(""), false)
    }

    @Test
    fun `date of birth field is not empty return true` () {
        assertEquals(validateDateOfBirth("20/20/2020"), true)
    }

    /*** gender field validation **/
    @Test
    fun `selected gender not equal to Male, Female, or Others return false` () {
        assertEquals(validateGender("Choose a gender"), false)
    }

    @Test
    fun `selected gender equal to Male, Female, or Others return true` () {
        assertEquals(validateGender("Male"), true)
        assertEquals(validateGender("Female"), true)
        assertEquals(validateGender("Others"), true)
    }


    /*** religion field validation **/
    @Test
    fun `selected religion not equal to Christian, Muslim, or Others return false` () {
        assertEquals(validateReligion("Choose a religion"), false)
    }

    @Test
    fun `selected religion equal to Christian, Muslim, or Others return true` () {
        assertEquals(validateReligion("Christian"), true)
        assertEquals(validateReligion("Muslim"), true)
        assertEquals(validateReligion("Others"), true)
    }

    /*** address field validation **/
    @Test
    fun `address field empty return false` () {
        assertEquals(validateAddress(""), false)
    }

    @Test
    fun `address field not empty return true` () {
        assertEquals(validateAddress("Sangotedo"), true)
    }


    /*** address field validation **/
    @Test
    fun `account number field empty return false` () {
        assertEquals(validateAccountNumber(""), false)
    }

    @Test
    fun `account number field has less than 10 digits return false` () {
        assertEquals(validateAccountNumber("0101010"), false)
    }

    @Test
    fun `account number field has 10 digits return true` () {
        assertEquals(validateAccountNumber("0101010101"), true)
    }


}