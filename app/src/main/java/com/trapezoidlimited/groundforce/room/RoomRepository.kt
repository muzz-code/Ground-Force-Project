package com.trapezoidlimited.groundforce.room

import androidx.lifecycle.LiveData

interface RoomRepository {
    suspend fun addAgent(agent: RoomAgent)
    suspend fun addAdditionalDetail(additionalDetail: RoomAdditionalDetail)
    suspend fun updateAgent(agent: RoomAgent)
    suspend fun updateAdditionalDetail(additionalDetail: RoomAdditionalDetail)
    fun readAgent(): LiveData<List<RoomAgent>>
    fun readAdditionalDetail(): LiveData<List<RoomAdditionalDetail>>
}