package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.data.property_draft.FormTypeAndTitleEntity
import com.emplk.realestatemanager.data.property_draft.PropertyFormParamsRepositoryImpl
import com.emplk.realestatemanager.ui.add.FormType
import kotlinx.coroutines.flow.Flow

interface PropertyFormParamsRepository {
    fun setFormTitle(formTypeAndDraftTitle: FormTypeAndTitleEntity)
    fun getFormTitle(): Flow<FormTypeAndTitleEntity>
    fun resetFormTitle()
}
