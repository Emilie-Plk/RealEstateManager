package com.emplk.realestatemanager.data.property_draft

import com.emplk.realestatemanager.domain.property_draft.PropertyFormParamsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class PropertyFormParamsRepositoryImpl @Inject constructor() : PropertyFormParamsRepository {
    private val formTitleMutableStateFlow: MutableStateFlow<String?> = MutableStateFlow(null)
    override fun setFormTitle(title: String?) {
        formTitleMutableStateFlow.tryEmit(title)
    }

    override fun getFormTitle(): Flow<String> = formTitleMutableStateFlow.filterNotNull()

    override fun resetFormTitle() {
        formTitleMutableStateFlow.tryEmit(null)
    }
}