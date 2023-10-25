package com.emplk.realestatemanager.domain.property_draft

import javax.inject.Inject

class SetFormTitleUseCase @Inject constructor(
    private val propertyFormParamsRepository: PropertyFormParamsRepository
) {
    fun invoke(title: String?) {
        propertyFormParamsRepository.setFormTitle(title)
    }
}