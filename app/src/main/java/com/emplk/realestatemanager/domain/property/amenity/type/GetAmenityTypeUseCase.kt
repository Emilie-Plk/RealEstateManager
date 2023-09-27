package com.emplk.realestatemanager.domain.property.amenity.type

import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import javax.inject.Inject

class GetAmenityTypeUseCase @Inject constructor(
    private val amenityTypeRepository: AmenityTypeRepository,
) {
    fun invoke(): List<AmenityType> = amenityTypeRepository.getAmenityTypes()
}