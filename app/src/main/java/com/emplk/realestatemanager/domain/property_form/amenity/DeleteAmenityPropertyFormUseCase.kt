package com.emplk.realestatemanager.domain.property_form.amenity

import javax.inject.Inject

class DeleteAmenityPropertyFormUseCase @Inject constructor(
    private val amenityFormRepository: AmenityFormRepository,
) {
    suspend fun invoke(amenityFormId: Long): Int =
        amenityFormRepository.delete(amenityFormId)
}