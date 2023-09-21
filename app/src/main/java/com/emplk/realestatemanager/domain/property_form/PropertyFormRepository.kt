package com.emplk.realestatemanager.domain.property_form

import kotlinx.coroutines.flow.Flow

interface PropertyFormRepository {
    suspend fun add(propertyFormEntity: PropertyFormEntity): Long?

    suspend fun addPropertyFormWithDetails(propertyFormEntity: PropertyFormEntity): Long

    fun setPropertyFormProgress(isPropertyFormInProgress: Boolean)

    fun isPropertyFormInProgressAsFlow(): Flow<Boolean>

    suspend fun getPropertyFormByIdAsFlow(propertyFormId: Long): Flow<PropertyFormEntity>

    suspend fun exists(): Boolean

    suspend fun update(propertyFormEntity: PropertyFormEntity)

    suspend fun delete(propertyFormId: Long)
}
