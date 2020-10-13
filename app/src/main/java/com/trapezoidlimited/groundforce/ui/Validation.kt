package com.trapezoidlimited.groundforce.ui

class Validation {

    /**validate phone number**/
    companion object{
        fun validatePhoneNumber(num: String): Boolean {
            val nigeriaNumber = Regex("""^(\+?234|0)[897][01]\d{8}${'$'}""")
            return if (num.isEmpty()) {
                false
            }
            else if(!num.matches(nigeriaNumber)){
                return false
            }else{
                true
            }
        }
    }

}