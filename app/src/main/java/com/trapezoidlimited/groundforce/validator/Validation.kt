package com.trapezoidlimited.groundforce.validator

class Validation {


        /** validate email **/
        fun validateEmail(email:String): Boolean {

            val emailPattern = Regex("""^(\w+\.?(\w+)?@([a-zA-Z_]+\.){1,2}[a-zA-Z]{2,6})${'$'}""")

            return if (email.isEmpty()) {
                false
            }
            else if(!email.matches(emailPattern)){
                return false
            }
            else {
                true
            }
        }

        fun validatePin(pin:String):Boolean{
            return !(pin.length < 4)
        }

}