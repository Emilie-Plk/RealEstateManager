package com.emplk.realestatemanager.domain.get_properties

import com.emplk.realestatemanager.domain.property.PropertyRepository
import javax.inject.Inject

class GetPropertiesAsFlowUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
) {
    fun invoke() = propertyRepository.getPropertiesAsFlow()
}
