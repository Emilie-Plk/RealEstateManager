package com.emplk.realestatemanager.domain.property_type

import kotlinx.coroutines.flow.Flow

interface PropertyTypeRepository {
    fun getPropertyTypeListFlow(): Flow<Map<Long, String>>
}
