package com.emplk.realestatemanager.domain.property

import javax.inject.Inject

class AddPropertyUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
) {
    suspend fun invoke(property: PropertyEntity): Long = propertyRepository.add(property)
}