package com.emplk.realestatemanager.domain.amenity.type

import javax.inject.Inject

class GetAmenityTypeFlowUseCase @Inject constructor(
    private val amenityTypeRepository: AmenityTypeRepository,
) {
    fun invoke() = amenityTypeRepository.getAmenityTypesAsFlow()
}