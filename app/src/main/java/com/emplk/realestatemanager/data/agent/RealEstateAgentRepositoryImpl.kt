package com.emplk.realestatemanager.data.agent

import com.emplk.realestatemanager.domain.agent.RealEstateAgentRepository
import javax.inject.Inject

class RealEstateAgentRepositoryImpl @Inject constructor() : RealEstateAgentRepository {
    private val agents: List<RealEstateAgent> = listOf(
        RealEstateAgent.JOHN_DOE,
        RealEstateAgent.JANE_DOE,
        RealEstateAgent.JOHN_SMITH,
        RealEstateAgent.JANE_SMITH,
        RealEstateAgent.JOHN_WAYNE,
        RealEstateAgent.JANE_WAYNE,
    )

    override fun getRealEstateAgents(): List<RealEstateAgent> = agents
}

enum class RealEstateAgent(val id: Long, val agentName: String) {
    JOHN_DOE(1L, "John Doe"),
    JANE_DOE(2L, "Jane Doe"),
    JOHN_SMITH(3L, "John Smith"),
    JANE_SMITH(4L, "Jane Smith"),
    JOHN_WAYNE(5L, "John Wayne"),
    JANE_WAYNE(6L, "Jane Wayne"),
}