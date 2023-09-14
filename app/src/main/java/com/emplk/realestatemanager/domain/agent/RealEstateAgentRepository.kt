package com.emplk.realestatemanager.domain.agent

import kotlinx.coroutines.flow.Flow

interface RealEstateAgentRepository {
    fun getAgentNameMapFlow(): Flow<Map<Long, String>>
}
