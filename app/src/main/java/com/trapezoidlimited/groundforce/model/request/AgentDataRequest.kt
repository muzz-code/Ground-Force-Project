package com.trapezoidlimited.groundforce.model.request

data class AgentDataRequest(
    var lastName: String,
    var firstName: String,
    var phoneNumber: String,
    var gender: String,
    var dob: String,
    var email: String,
    var password: String,
    var residentialAddress: String,
    var state: String,
    var lga: String,
    var zipCode: String,
    var longitude: String,
    var latitude:	String,
    val roles: List<String>
)



