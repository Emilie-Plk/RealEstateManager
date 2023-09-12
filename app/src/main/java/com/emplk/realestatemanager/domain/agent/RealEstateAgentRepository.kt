package com.emplk.realestatemanager.domain.agent

import kotlinx.coroutines.flow.Flow

interface RealEstateAgentRepository {
    fun getAgentNameListFlow(): Flow<List<String>>
}
