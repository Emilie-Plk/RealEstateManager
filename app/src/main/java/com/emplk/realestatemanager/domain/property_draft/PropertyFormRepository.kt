package com.emplk.realestatemanager.domain.property_draft

import kotlinx.coroutines.flow.Flow

interface PropertyFormRepository {
    suspend fun add(propertyDraftEntity: PropertyDraftEntity): Long?

    suspend fun addPropertyFormWithDetails(propertyDraftEntity: PropertyDraftEntity): Long

    fun setPropertyFormProgress(isPropertyFormInProgress: Boolean)

    fun isPropertyFormInProgressAsFlow(): Flow<Boolean>

    suspend fun getExistingPropertyFormId(): Long?

    suspend fun getExistingPropertyForm(): PropertyDraftEntity?

    suspend fun getPropertyFormById(propertyFormId: Long): PropertyDraftEntity

    suspend fun exists(): Boolean

    suspend fun update(propertyDraftEntity: PropertyDraftEntity, propertyFormId: Long)

    suspend fun delete(propertyFormId: Long): Boolean
}
