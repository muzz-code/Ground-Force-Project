package com.trapezoidlimited.groundforce.utils

import com.google.common.truth.Truth.assertThat
import com.trapezoidlimited.groundforce.validator.ValidationPhoneNumber
import org.junit.Test

class ValidationPhoneNumberTest {

    @Test
    fun `phone number field is empty return false` () {
        val result = ValidationPhoneNumber.validatePhoneNumber("")
        assertThat(result).isFalse()
    }

    @Test
    fun `phone number start with +234` () {
        val result = ValidationPhoneNumber.validatePhoneNumber("+2348044678934")
        assertThat(result).isTrue()
    }

    @Test
    fun `phone number not start with +234 return false` () {
        val result = ValidationPhoneNumber.validatePhoneNumber("4567890450")
        assertThat(result).isFalse()
    }

    @Test
    fun `phone number less that ten digit return false` () {
        val result = ValidationPhoneNumber.validatePhoneNumber("+2345678")
        assertThat(result).isFalse()
    }

    @Test
    fun `phone number start with +234 and contain thirteen digit numbers return true` () {
        val result = ValidationPhoneNumber.validatePhoneNumber("+2348045336711")
        assertThat(result).isTrue()
    }

}