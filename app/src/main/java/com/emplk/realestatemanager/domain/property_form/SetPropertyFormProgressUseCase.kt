package com.emplk.realestatemanager.domain.property_form

import javax.inject.Inject

class SetPropertyFormProgressUseCase @Inject constructor(
    private val propertyFormRepository: PropertyFormRepository,
) {
    fun invoke(isPropertyFormInProgress: Boolean) =
        propertyFormRepository.setPropertyFormProgress(isPropertyFormInProgress)
}