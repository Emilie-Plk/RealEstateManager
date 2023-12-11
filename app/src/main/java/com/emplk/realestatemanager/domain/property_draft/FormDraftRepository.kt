package com.emplk.realestatemanager.domain.property_draft

interface FormDraftRepository {
    suspend fun add(formDraftEntity: FormDraftEntity): Long

    suspend fun addFormDraftWithDetails(formDraftEntity: FormDraftEntity): Long

    suspend fun doesDraftExist(formId: Long?): Boolean

    suspend fun doesPropertyExist(formId: Long?): Boolean

    suspend fun getFormDraftEntityById(formId: Long): FormDraftEntity

    suspend fun getDraftsCount(): Int

    suspend fun getFormsWithDetails(): List<FormWithDetailEntity>

    suspend fun update(formDraftEntity: FormDraftEntity)

    suspend fun updateAddressValidity(formId: Long, isAddressValid: Boolean)

    suspend fun delete(formId: Long): Boolean
}
