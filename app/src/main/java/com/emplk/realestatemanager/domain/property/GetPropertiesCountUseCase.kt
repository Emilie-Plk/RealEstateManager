package com.emplk.realestatemanager.domain.property

import javax.inject.Inject

class GetPropertiesCountUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository
) {
    fun invoke() = propertyRepository.getPropertiesCountAsFlow()
}
