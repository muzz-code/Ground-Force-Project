package com.trapezoidlimited.groundforce.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "history_survey_table")
class RoomHistorySurvey(
    @PrimaryKey
    var Id: String,
    var title: String,
    var body: String,
)