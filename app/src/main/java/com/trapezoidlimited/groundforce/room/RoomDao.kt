package com.trapezoidlimited.groundforce.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RoomDao {

    //Add Agent to Database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAgent(agent: RoomAgent)

    //Add Additional Information
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAdditionalDetail(additionalDetail: RoomAdditionalDetail)

    @Update
    suspend fun updateAgent(vararg agent: RoomAgent)

    @Update
    suspend fun updateAdditionalDetail(vararg additionalDetail: RoomAdditionalDetail)

    //Select Agent from Room
    @Query("SELECT * FROM agent_table")
    fun readAgent(): LiveData<List<RoomAgent>>

    //Select Additional Detail
    @Query("SELECT * FROM additional_agent_table")
    fun readAdditionalDetail(): LiveData<List<RoomAdditionalDetail>>

}