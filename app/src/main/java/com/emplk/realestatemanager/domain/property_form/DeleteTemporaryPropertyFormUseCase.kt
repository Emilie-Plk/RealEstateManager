package com.emplk.realestatemanager.domain.property_form

import javax.inject.Inject

class DeleteTemporaryPropertyFormUseCase @Inject constructor(
    private val propertyFormRepository: PropertyFormRepository,
    private val getCurrentPropertyFormIdUseCase: GetCurrentPropertyFormIdUseCase,
) {
    suspend fun invoke() {
        getCurrentPropertyFormIdUseCase.invoke()?.let {
            propertyFormRepository.delete(it)
        }
    }
}