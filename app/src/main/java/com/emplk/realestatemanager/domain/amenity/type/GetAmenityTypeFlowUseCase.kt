package com.emplk.realestatemanager.domain.amenity.type

import com.emplk.realestatemanager.domain.amenity.AmenityType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAmenityTypeFlowUseCase @Inject constructor(
    private val amenityTypeRepository: AmenityTypeRepository,
) {
    fun invoke(): Flow<List<AmenityType>> = amenityTypeRepository.getAmenityTypesAsFlow()
}