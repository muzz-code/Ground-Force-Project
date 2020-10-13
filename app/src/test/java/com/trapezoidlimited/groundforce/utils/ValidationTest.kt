package com.trapezoidlimited.groundforce.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidationTest {

    @Test
    fun `phone number field is empty return false` () {
        val result = Validation.validatePhoneNumber("")
        assertThat(result).isFalse()
    }

    @Test
    fun `phone number start with +234` () {
        val result = Validation.validatePhoneNumber("+2348044678934")
        assertThat(result).isTrue()
    }

    @Test
    fun `phone number not start with +234 return false` () {
        val result = Validation.validatePhoneNumber("4567890450")
        assertThat(result).isFalse()
    }

    @Test
    fun `phone number less that ten digit return false` () {
        val result = Validation.validatePhoneNumber("+2345678")
        assertThat(result).isFalse()
    }

    @Test
    fun `phone number start with +234 and contain thirteen digit numbers return true` () {
        val result = Validation.validatePhoneNumber("+2348045336711")
        assertThat(result).isTrue()
    }

}