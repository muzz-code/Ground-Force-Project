package com.trapezoidlimited.groundforce.validator


import org.junit.Test

import org.junit.Assert.*

class ResetPasswordValidatorTest{
    @Test
    fun new_password_with_string(){
         //given
        var newPassword="hello123"
        assertTrue(ResetPasswordValidator.isNotEmpty(newPassword))
    }

    @Test
    fun new_password_with_empty_string(){
        var newPassword=""
        assertFalse(ResetPasswordValidator.isNotEmpty(newPassword))
    }

    @Test
    fun confirm_password_with_string(){
     var confirmPassword="hello123"
     assertTrue(ResetPasswordValidator.isNotEmpty(confirmPassword))
    }

    @Test
    fun confirm_password_with_empty_string(){
        var confirmPasswordEmpty=""
        assertFalse(ResetPasswordValidator.isNotEmpty(confirmPasswordEmpty))
    }

    @Test
    fun newpassword_and_confirmpassword_match(){
        var newPassword="hello123"
        var confirmPassword="hello123"
        assertTrue(ResetPasswordValidator.isEqual(newPassword,confirmPassword))
    }


    @Test
    fun newpassword_and_confirmpassword_mismatch(){
        var newPassword="hello1233"
        var confirmPasword="hello123"
        assertFalse(ResetPasswordValidator.isEqual(newPassword,confirmPasword))
    }




}