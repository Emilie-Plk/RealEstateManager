package com.emplk.realestatemanager.domain.property_form

import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.property_form.location.LocationFormEntity
import com.emplk.realestatemanager.domain.property_form.picture_preview.PicturePreviewEntity
import java.math.BigDecimal

data class PropertyFormEntity(
    val type: String?,
    val price: String?,
    val surface: String?,
    val address: String?,
    val rooms: Int?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val description: String?,
    val agentName: String?,
    val pictures: List<PicturePreviewEntity> = emptyList(),
    val amenities: List<AmenityEntity> = emptyList(),
)
