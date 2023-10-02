package com.emplk.realestatemanager.domain.property_form

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedPropertyFormEventUseCase @Inject constructor(private val propertyFormRepository: PropertyFormRepository) {

    fun invoke(): Flow<Unit> {
        return propertyFormRepository.getSavedPropertyFormEvent()
    }
}