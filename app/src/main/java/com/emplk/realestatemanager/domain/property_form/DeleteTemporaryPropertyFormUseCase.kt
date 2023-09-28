package com.emplk.realestatemanager.domain.property_form

import javax.inject.Inject

class DeleteTemporaryPropertyFormUseCase @Inject constructor(
    private val propertyFormRepository: PropertyFormRepository,
    private val getCurrentPropertyFormIdUseCase: GetCurrentPropertyFormIdUseCase,
) {
    suspend fun invoke(): Boolean? {
        val propertyFormIdDeferred = getCurrentPropertyFormIdUseCase.invoke()
        if (propertyFormIdDeferred != null) {
            propertyFormRepository.delete(propertyFormIdDeferred)
            return true
        }
        return false
    }
}