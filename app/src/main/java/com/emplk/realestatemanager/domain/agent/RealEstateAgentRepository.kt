package com.emplk.realestatemanager.domain.agent

interface RealEstateAgentRepository {
    fun getAgentNameMap(): Map<Long, String>
}
