package com.emplk.realestatemanager.domain.property_draft

interface PropertyFormRepository {
    suspend fun add(propertyDraftEntity: PropertyDraftEntity): Long?

    suspend fun addPropertyFormWithDetails(propertyDraftEntity: PropertyDraftEntity): Long

    suspend fun getExistingPropertyFormId(): Long?

    suspend fun getAddFormId(): Long?

    suspend fun doesPropertyDraftExist(propertyFormId: Long): Boolean

    suspend fun doesPropertyExistInBothTables(propertyFormId: Long): Boolean

    suspend fun getExistingPropertyForm(): PropertyDraftEntity?

    suspend fun getPropertyFormById(propertyFormId: Long): PropertyDraftEntity

    suspend fun update(propertyDraftEntity: PropertyDraftEntity, propertyFormId: Long)

    suspend fun updateIsAddressValid(propertyFormId: Long, isAddressValid: Boolean)

    suspend fun delete(propertyFormId: Long): Boolean
}
