package com.emplk.realestatemanager.domain.property_form

import javax.inject.Inject

class InitTemporaryPropertyFormUseCase @Inject constructor(
    private val propertyFormRepository: PropertyFormRepository,
    private val addTemporaryPropertyFormUseCase: AddTemporaryPropertyFormUseCase,
    private val hasAlreadyPropertyFormInDatabaseUseCase: HasAlreadyPropertyFormInDatabaseUseCase,
) {

    suspend fun invoke(): PropertyFormDatabaseState {
        val hasAlreadyPropertyFormInDb = hasAlreadyPropertyFormInDatabaseUseCase.invoke()
        return if (!hasAlreadyPropertyFormInDb) {
            val newPropertyFormId = addTemporaryPropertyFormUseCase.invoke()
            PropertyFormDatabaseState.Empty(newPropertyFormId)
        } else {
            val existingPropertyFormId = propertyFormRepository.getExistingPropertyFormId()
            val existingPropertyFormEntity = propertyFormRepository.getPropertyFormByIdAsFlow(existingPropertyFormId)
            PropertyFormDatabaseState.DraftAlreadyExists(existingPropertyFormEntity)
        }
    }
}