package com.emplk.realestatemanager.domain.property_draft

interface FormDraftRepository {
    suspend fun add(formDraftEntity: FormDraftEntity): Long

    suspend fun addPropertyFormWithDetails(formDraftEntity: FormDraftEntity): Long

    suspend fun doesDraftExist(propertyFormId: Long?): Boolean

    suspend fun doesPropertyExist(propertyFormId: Long?): Boolean

    suspend fun getPropertyFormById(propertyFormId: Long): FormDraftEntity

    suspend fun getDraftsCount(): Int

    suspend fun getDraftsWithFeaturePicture(): List<FormWithTitleDateAndFeaturedPictureEntity>

    suspend fun update(formDraftEntity: FormDraftEntity)

    suspend fun updateIsAddressValid(propertyFormId: Long, isAddressValid: Boolean)

    suspend fun delete(propertyFormId: Long): Boolean
}
