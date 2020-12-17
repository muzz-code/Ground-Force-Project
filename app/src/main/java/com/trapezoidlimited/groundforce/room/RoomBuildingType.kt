package com.trapezoidlimited.groundforce.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "building_type_table")
class RoomBuildingType(
    @PrimaryKey
    val typeID: String,
    val typeName: String
)