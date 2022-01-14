package com.trapezoidlimited.groundforce.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "survey_table")
class RoomSurvey(
    @PrimaryKey
    var Id: String,
    var title: String,
    var body: String,
)

