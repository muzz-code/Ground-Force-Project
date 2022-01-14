package com.trapezoidlimited.groundforce.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.trapezoidlimited.groundforce.model.response.ParentMission


@Entity(tableName = "history_mission_table")
data class RoomHistoryMission(
    @PrimaryKey
    var Id: String,
    val locationTitle: String,
    val locationSubTitle: String,
    val date: String? = null
): ParentMission
