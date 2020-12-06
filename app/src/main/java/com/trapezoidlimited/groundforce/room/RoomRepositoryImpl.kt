package com.trapezoidlimited.groundforce.room

import androidx.lifecycle.LiveData

class RoomRepositoryImpl(private val dao: RoomDao) : RoomRepository {
    override suspend fun addAgent(agent: RoomAgent) {
        dao.addAgent(agent)
    }

    override suspend fun addAdditionalDetail(additionalDetail: RoomAdditionalDetail) {
        dao.addAdditionalDetail(additionalDetail)
    }

    override suspend fun updateAgent(agent: RoomAgent) {
        dao.updateAgent(agent)
    }

    override suspend fun updateAdditionalDetail(additionalDetail: RoomAdditionalDetail) {
        dao.updateAdditionalDetail(additionalDetail)
    }

    override suspend fun addMission(mission: RoomMission) {
        dao.addMission(mission)
    }

    override fun readAgent(): LiveData<List<RoomAgent>> {
        return dao.readAgent()
    }

    override fun readAdditionalDetail(): LiveData<List<RoomAdditionalDetail>> {
        return dao.readAdditionalDetail()
    }

    override fun readAllMissions(): LiveData<List<RoomMission>> {
       return dao.readAllMissions()
    }

    override fun readAllOngoingMissions(): LiveData<List<RoomOngoingMission>> {
        return dao.readAllOngoingMissions()
    }

    override suspend fun deleteAllMissions(){
        dao.deleteAllMissions()
    }

    override suspend fun deleteByMissionId(missionId: String) {
        dao.deleteByMissionId(missionId)
    }

    override suspend fun addOngoingMission(ongoingMission: RoomOngoingMission) {
        dao.addOngoingMission(ongoingMission)
    }

    override suspend fun deleteAllOngoingMissions() {
        dao.deleteAllOngoingMissions()
    }

    override suspend fun deleteByOngoingMissionId(missionId: String) {
        dao.deleteByOngoingMissionId(missionId)
    }


}