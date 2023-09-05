package com.emplk.realestatemanager.domain.property

import javax.inject.Inject

class GetPropertiesAsFlowUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
) {
    fun invoke() = propertyRepository.getPropertiesAsFlow()
}
