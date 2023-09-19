package com.emplk.realestatemanager.domain.property_form.amenity

import javax.inject.Inject

class AddAmenityPropertyFormUseCase @Inject constructor(
    private val amenityFormRepository: AmenityFormRepository,
) {
    suspend fun invoke(amenityFormEntity: AmenityFormEntity): Long =
        amenityFormRepository.add(amenityFormEntity)
}