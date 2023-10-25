package com.emplk.realestatemanager.domain.property_draft

import javax.inject.Inject

class ResetFormTitleUseCase @Inject constructor(
    private val propertyFormParamsRepository: PropertyFormParamsRepository
) {
    fun invoke() {
        propertyFormParamsRepository.resetFormTitle()
    }
}