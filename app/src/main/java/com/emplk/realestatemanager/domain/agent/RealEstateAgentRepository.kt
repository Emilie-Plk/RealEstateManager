package com.emplk.realestatemanager.domain.agent

import com.emplk.realestatemanager.data.agent.RealEstateAgent

interface RealEstateAgentRepository {
    fun getRealEstateAgents(): List<RealEstateAgent>
}
