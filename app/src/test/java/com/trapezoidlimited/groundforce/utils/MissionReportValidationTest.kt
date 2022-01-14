package com.trapezoidlimited.groundforce.utils

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class MissionReportValidationTest {

    private lateinit var test: MissionReportValidation
    @Before
    fun setUp(){
        test = MissionReportValidation()
    }

    @Test
    fun validateEmptyTextInput() {
        assertEquals(test.validateTextInput(""), false)
    }

    @Test
    fun validateTextInputWithContent() {
        assertEquals(test.validateTextInput("testing"), true)
    }
}