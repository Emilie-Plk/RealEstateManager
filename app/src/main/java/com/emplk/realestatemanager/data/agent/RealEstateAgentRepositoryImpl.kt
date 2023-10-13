package com.emplk.realestatemanager.data.agent

import com.emplk.realestatemanager.domain.agent.RealEstateAgentRepository
import javax.inject.Inject

class RealEstateAgentRepositoryImpl @Inject constructor() : RealEstateAgentRepository {
    private val agentNameList = mapOf(
        1L to "John Doe",
        2L to "Jane Doe",
        3L to "John Smith",
        4L to "Jane Smith",
        5L to "John Wayne",
        6L to "Jane Wayne",
    )

    override fun getAgentNameMap(): Map<Long, String> = agentNameList
}