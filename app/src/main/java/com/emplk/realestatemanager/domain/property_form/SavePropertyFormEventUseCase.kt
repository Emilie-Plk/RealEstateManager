package com.emplk.realestatemanager.domain.property_form

import javax.inject.Inject

class SavePropertyFormEventUseCase @Inject constructor(private val propertyFormRepository: PropertyFormRepository) {

    fun invoke() {
        propertyFormRepository.onSavePropertyFormEvent()
    }
}