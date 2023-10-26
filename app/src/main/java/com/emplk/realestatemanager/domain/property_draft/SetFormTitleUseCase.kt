package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.data.property_draft.FormTypeAndTitleEntity
import com.emplk.realestatemanager.ui.add.FormType
import javax.inject.Inject

class SetFormTitleUseCase @Inject constructor(
    private val propertyFormParamsRepository: PropertyFormParamsRepository
) {
    fun invoke(formType: FormType, title: String?) {
        propertyFormParamsRepository.setFormTitle(FormTypeAndTitleEntity(formType, title))
    }
}