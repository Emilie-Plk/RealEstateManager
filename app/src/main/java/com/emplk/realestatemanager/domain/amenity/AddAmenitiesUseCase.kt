package com.emplk.realestatemanager.domain.amenity

import javax.inject.Inject

class AddAmenitiesUseCase @Inject constructor(
    private val amenityRepository: AmenityRepository,
) {
    suspend fun invoke(amenityEntity: AmenityEntity) {
        amenityRepository.addAmenity(amenityEntity)
    }
}