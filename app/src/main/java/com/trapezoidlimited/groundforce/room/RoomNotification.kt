package com.trapezoidlimited.groundforce.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_table")
data class RoomNotification(
    @PrimaryKey
    val id: String,
    val message: String,
    val date: String,
    val isNew: String
)