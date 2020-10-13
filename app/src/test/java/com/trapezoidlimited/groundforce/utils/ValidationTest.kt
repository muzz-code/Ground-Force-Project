package com.trapezoidlimited.groundforce.utils

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class ValidationTest {
    private lateinit var test: Validation
    @Before
    fun setUp(){
        test = Validation()
    }
/***email validation**/
    @Test
    fun forEmptyString() {
        assertEquals(test.validateEmail(""), false)
    }

    @Test
    fun forWrongEmailFormat() {
        assertEquals(test.validateEmail("fredgmail.com"), false)
    }

    @Test
    fun forCorrectEmailFormat() {
        assertEquals(test.validateEmail("fred@gmail.com"), true)
    }

    /***password validation**/
    @Test
    fun forEmptyPinString() {
        assertEquals(test.validatePin(""), false)
    }
    @Test
    fun forPinLessThanFourDigits() {
        assertEquals(test.validatePin("123"), false)
    }
    @Test
    fun forCorrectPin() {
        assertEquals(test.validatePin("1234"), true)
    }



}