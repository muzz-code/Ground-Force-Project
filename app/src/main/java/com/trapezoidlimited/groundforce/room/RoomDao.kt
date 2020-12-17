package com.trapezoidlimited.groundforce.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.trapezoidlimited.groundforce.model.mission.MissionItem
import com.trapezoidlimited.groundforce.model.response.BuildingType

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

    @Query("SELECT * FROM agent_table")
    suspend fun readAgentA(): List<RoomAgent>

    //Select Additional Detail
    @Query("SELECT * FROM additional_agent_table")
    fun readAdditionalDetail(): LiveData<List<RoomAdditionalDetail>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMission(mission: RoomMission)

    @Query("SELECT * FROM mission_table")
    fun readAllMissions(): LiveData<List<RoomMission>>

    @Query("DELETE FROM mission_table")
    suspend fun deleteAllMissions()

    @Query("DELETE FROM mission_table WHERE id = :missionId")
    suspend fun deleteByMissionId(missionId: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addOngoingMission(ongoingMission: RoomOngoingMission)

    @Query("SELECT * FROM ongoing_mission_table")
    fun readAllOngoingMissions(): LiveData<List<RoomOngoingMission>>

    @Query("DELETE FROM ongoing_mission_table")
    suspend fun deleteAllOngoingMissions()

    @Query("DELETE FROM ongoing_mission_table WHERE id = :missionId")
    suspend fun deleteByOngoingMissionId(missionId: String)

    @Query("DELETE FROM agent_table")
    suspend fun deleteAllAgentDetails()


}