package com.trapezoidlimited.groundforce.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "additional_agent_table")
class RoomAdditionalDetail(
    @PrimaryKey
    var agentId: Int = 1,
    var bankCode: String,
    var accountNumber: String,
    var religion: String,
    var additionalPhoneNumber: String?,
    var gender: String,
    var avatarUrl: String,
    var publicId: String,
    var accountName: String? = null
)