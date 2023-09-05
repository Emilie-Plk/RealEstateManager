package com.emplk.realestatemanager.domain.amenity

import javax.inject.Inject

class AddAmenityUseCase @Inject constructor(
    private val amenityRepository: AmenityRepository
) {
    suspend fun invoke(amenity: AmenityEntity) {
        amenityRepository.addAmenity(amenity)
    }
}
