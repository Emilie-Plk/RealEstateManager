package com.emplk.realestatemanager.domain.property_form

import javax.inject.Inject

class HasAlreadyPropertyFormInDatabaseUseCase @Inject constructor(
    private val propertyFormRepository: PropertyFormRepository
) {
    suspend fun invoke(): Boolean = propertyFormRepository.exists()
}