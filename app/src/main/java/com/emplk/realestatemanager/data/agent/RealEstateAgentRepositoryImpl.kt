package com.emplk.realestatemanager.data.agent

import com.emplk.realestatemanager.domain.agent.RealEstateAgentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RealEstateAgentRepositoryImpl @Inject constructor() : RealEstateAgentRepository {
    private val agentNameList = listOf(
        "John Doe",
        "Jane Doe",
        "John Smith",
        "Jane Smith",
        "John Johnson",
        "Jane Johnson",
    )

    override fun getAgentNameListFlow(): Flow<List<String>> = flow {
        emit(agentNameList)
    }
}