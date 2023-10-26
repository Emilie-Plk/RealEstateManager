package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.data.property_draft.FormTypeAndTitleEntity
import com.emplk.realestatemanager.ui.add.FormType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class GetFormTitleAsFlowUseCase @Inject constructor(
    private val propertyFormParamsRepository: PropertyFormParamsRepository
) {
    fun invoke() : Flow<FormTypeAndTitleEntity> {
        return propertyFormParamsRepository.getFormTitle().distinctUntilChanged()
    }
}