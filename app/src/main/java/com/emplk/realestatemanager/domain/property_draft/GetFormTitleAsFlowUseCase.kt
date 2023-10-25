package com.emplk.realestatemanager.domain.property_draft

import javax.inject.Inject

class GetFormTitleAsFlowUseCase @Inject constructor(
    private val propertyFormParamsRepository: PropertyFormParamsRepository
) {
    fun invoke() = propertyFormParamsRepository.getFormTitle()
}