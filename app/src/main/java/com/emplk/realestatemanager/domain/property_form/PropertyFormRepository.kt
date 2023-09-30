package com.emplk.realestatemanager.domain.property_form

import kotlinx.coroutines.flow.Flow

interface PropertyFormRepository {
    suspend fun add(propertyFormEntity: PropertyFormEntity): Long?

    suspend fun addPropertyFormWithDetails(propertyFormEntity: PropertyFormEntity): Long

    fun setPropertyFormProgress(isPropertyFormInProgress: Boolean)

    fun isPropertyFormInProgressAsFlow(): Flow<Boolean>

    suspend fun getExistingPropertyFormId(): Long?

    suspend fun getExistingPropertyForm(): PropertyFormEntity?

    suspend fun getPropertyFormById(propertyFormId: Long): PropertyFormEntity

    suspend fun exists(): Boolean

    suspend fun update(propertyFormEntity: PropertyFormEntity, propertyFormId: Long)

    suspend fun delete(propertyFormId: Long): Boolean

    fun onSavePropertyFormEvent()

    fun getSavedPropertyFormEvent(): Flow<Unit>
}
