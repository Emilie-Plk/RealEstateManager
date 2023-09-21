package com.emplk.realestatemanager.domain.property_form

import kotlinx.coroutines.flow.Flow

interface PropertyFormRepository {
    suspend fun add(propertyFormEntity: PropertyFormEntity): Long?

    suspend fun addPropertyFormWithDetails(propertyFormEntity: PropertyFormEntity): Boolean

    suspend fun getPropertyFormByIdAsFlow(propertyFormId: Long): Flow<PropertyFormEntity>

    suspend fun exists(): Boolean

    suspend fun update(propertyFormEntity: PropertyFormEntity)
}
