package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.property_draft.model.FormDraftEntity
import com.emplk.realestatemanager.domain.property_draft.model.FormWithDetailEntity

interface FormDraftRepository {
    suspend fun add(formDraftEntity: FormDraftEntity): Long

    suspend fun addFormWithDetails(formDraftEntity: FormDraftEntity): Long

    suspend fun doesFormExist(formId: Long?): Boolean

    suspend fun doesPropertyExist(formId: Long?): Boolean

    suspend fun getFormDraftEntityById(formId: Long): FormDraftEntity

    suspend fun getFormsCount(): Int

    suspend fun getFormsWithDetails(): List<FormWithDetailEntity>

    suspend fun getEmptyFormId(): Long?

    suspend fun update(formDraftEntity: FormDraftEntity)

    suspend fun updateAddressValidity(formId: Long, isAddressValid: Boolean)

    suspend fun delete(formId: Long): Boolean
}
