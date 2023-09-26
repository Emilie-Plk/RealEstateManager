package com.emplk.realestatemanager.domain.property_form

import com.emplk.realestatemanager.domain.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.property_form.location.LocationFormEntity
import com.emplk.realestatemanager.domain.property_form.picture_preview.PicturePreviewEntity
import java.math.BigDecimal

data class PropertyFormEntity(
    val type: String?,
    val price: BigDecimal? = BigDecimal.ZERO,
    val surface: Int?,
    val rooms: Int?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val description: String?,
    val agentName: String?,
    val location: LocationFormEntity?,  // TODO: NOT SURE
    val pictures: List<PicturePreviewEntity> = emptyList(),
    val amenities: List<AmenityEntity> = emptyList(),
)
