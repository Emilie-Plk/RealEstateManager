package com.emplk.realestatemanager.domain.property_form

import com.emplk.realestatemanager.domain.property_form.amenity.AmenityFormEntity
import com.emplk.realestatemanager.domain.property_form.picture_preview.PicturePreviewEntity
import javax.inject.Inject

class AddTemporaryPropertyFormUseCase @Inject constructor(
    private val propertyFormRepository: PropertyFormRepository
) {
    suspend fun invoke(): Long = propertyFormRepository.addPropertyFormWithDetails(PropertyFormEntity())

}
