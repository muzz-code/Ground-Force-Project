package com.trapezoidlimited.groundforce.validator

class ResetPasswordValidator {

    companion object {
        fun isNotEmpty(password: String):Boolean{
            return password.isNotEmpty()
        }
        fun isEqual(newPassword:String,confirmPassword:String):Boolean{
            return newPassword==confirmPassword
        }
    }
}
