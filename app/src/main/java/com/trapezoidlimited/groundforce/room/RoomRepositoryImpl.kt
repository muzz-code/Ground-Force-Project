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

    override fun readAgent(): LiveData<List<RoomAgent>> {
        return dao.readAgent()
    }

    override fun readAdditionalDetail(): LiveData<List<RoomAdditionalDetail>> {
        return dao.readAdditionalDetail()
    }
}