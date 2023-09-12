package com.emplk.realestatemanager.domain.agent

import com.emplk.realestatemanager.ui.utils.NativeText
import kotlinx.coroutines.flow.Flow

interface RealEstateAgentRepository {
    fun getAgentNameMapFlow(): Flow<Map<Long, String>>
}
