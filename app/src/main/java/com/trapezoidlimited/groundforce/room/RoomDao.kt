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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addHistoryMission(mission: RoomHistoryMission)

    @Query("SELECT * FROM history_mission_table")
    fun readAllHistoryMissions(): LiveData<List<RoomHistoryMission>>

    @Query("DELETE FROM history_mission_table")
    suspend fun deleteAllHistoryMissions()

    @Query("DELETE FROM history_mission_table WHERE id = :historyMissionId")
    suspend fun deleteByHistoryMissionId(historyMissionId: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addHistorySurvey(mission: RoomHistorySurvey)

    @Query("SELECT * FROM history_survey_table")
    fun readAllHistorySurveys(): LiveData<List<RoomHistorySurvey>>

    @Query("DELETE FROM history_survey_table")
    suspend fun deleteAllHistorySurveys()

    @Query("DELETE FROM history_survey_table WHERE id = :historySurveyId")
    suspend fun deleteByHistorySurveyId(historySurveyId: String)


    @Query("DELETE FROM agent_table")
    suspend fun deleteAllAgentDetails()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSurvey(survey: RoomSurvey)

    @Query("SELECT * FROM survey_table")
    fun readAllSurveys(): LiveData<List<RoomSurvey>>

    @Query("DELETE FROM survey_table")
    suspend fun deleteAllSurveys()

    @Query("DELETE FROM survey_table WHERE id = :surveyId")
    suspend fun deleteBySurveyId(surveyId: String)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNotification(notification: RoomNotification)

    @Query("SELECT * FROM notification_table")
    fun readAllAddNotifications(): LiveData<List<RoomNotification>>

    @Query("DELETE FROM notification_table")
    suspend fun deleteAllAddNotifications()

    @Query("DELETE FROM notification_table WHERE id = :notificationId")
    suspend fun deleteByNotificationId(notificationId: String)



}