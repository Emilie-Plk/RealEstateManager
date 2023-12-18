package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.domain.property_type.PropertyTypeRepository
import javax.inject.Inject

class GetPropertyTypeForFilterUseCase @Inject constructor(
    private val propertyTypeRepository: PropertyTypeRepository
) {
    // TODO: got rid?..
    fun invoke(): Map<Long, String> = mapOf()
}
