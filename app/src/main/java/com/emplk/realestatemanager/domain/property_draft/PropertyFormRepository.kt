package com.emplk.realestatemanager.domain.property_draft

interface PropertyFormRepository {
    suspend fun add(propertyDraftEntity: PropertyDraftEntity): Long?

    suspend fun addPropertyFormWithDetails(propertyDraftEntity: PropertyDraftEntity): Long

    suspend fun getExistingPropertyFormId(): Long?

    suspend fun getExistingPropertyForm(): PropertyDraftEntity?

    suspend fun getPropertyFormById(propertyFormId: Long): PropertyDraftEntity

    suspend fun exists(): Boolean

    suspend fun update(propertyDraftEntity: PropertyDraftEntity, propertyFormId: Long)

    suspend fun updateIsAddressValid(propertyFormId: Long, isAddressValid: Boolean)

    suspend fun delete(propertyFormId: Long): Boolean
}
