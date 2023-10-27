package com.emplk.realestatemanager.data.property_draft

import com.emplk.realestatemanager.domain.property_draft.PropertyFormParamsRepository
import com.emplk.realestatemanager.ui.add.FormType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class PropertyFormParamsRepositoryImpl @Inject constructor() : PropertyFormParamsRepository {
    private val formTitleMutableStateFlow: MutableStateFlow<FormTypeAndTitleEntity?> =
        MutableStateFlow(FormTypeAndTitleEntity(FormType.UNKNOWN, null))

    override fun setFormTitle(formTypeAndDraftTitle: FormTypeAndTitleEntity) {
        formTitleMutableStateFlow.tryEmit(formTypeAndDraftTitle)
    }

    override fun getFormTitle(): Flow<FormTypeAndTitleEntity> = formTitleMutableStateFlow.filterNotNull()

    override fun resetFormTitle() {
        formTitleMutableStateFlow.tryEmit(null)
    }
}