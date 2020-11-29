package com.trapezoidlimited.groundforce.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agent_table")
class RoomAgent(
    @PrimaryKey
    var agentId: Int = 1,
    val lastName: String,
    val firstName: String,
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
    var latitude: String,
)