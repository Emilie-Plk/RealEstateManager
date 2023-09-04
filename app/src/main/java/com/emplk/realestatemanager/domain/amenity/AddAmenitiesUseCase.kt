package com.emplk.realestatemanager.domain.amenity

import com.emplk.realestatemanager.data.amenity.AmenityDtoEntity
import javax.inject.Inject

class AddAmenitiesUseCase @Inject constructor(
    private val amenityRepository: AmenityRepository,
) {
    suspend fun invoke(amenityDtoEntity: AmenityDtoEntity) {
        amenityRepository.addAmenity(amenityDtoEntity)
    }
}