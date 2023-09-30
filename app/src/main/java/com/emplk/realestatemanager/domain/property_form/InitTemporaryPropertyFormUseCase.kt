package com.emplk.realestatemanager.domain.property_form

import javax.inject.Inject

class InitTemporaryPropertyFormUseCase @Inject constructor(
    private val propertyFormRepository: PropertyFormRepository,
    private val addTemporaryPropertyFormUseCase: AddTemporaryPropertyFormUseCase,
) {

    suspend fun invoke(): PropertyFormDatabaseState {
        val existingPropertyFormEntity = propertyFormRepository.getExistingPropertyForm()
        return if (existingPropertyFormEntity != null) {
            PropertyFormDatabaseState.DraftAlreadyExists(existingPropertyFormEntity)
        } else {
            PropertyFormDatabaseState.Empty(addTemporaryPropertyFormUseCase.invoke())
        }
    }
}