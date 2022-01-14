package com.trapezoidlimited.groundforce.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agent_table")
class RoomAgent(
    @PrimaryKey
    var agentId: Int = 1,
    val id: String,
    val lastName: String,
    val firstName: String,
    var gender: String,
    var dob: String,
    var email: String,
    var residentialAddress: String
)

