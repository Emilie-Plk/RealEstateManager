package com.emplk.realestatemanager.domain.property_form

import com.emplk.realestatemanager.domain.property_form.amenity.AmenityFormEntity
import com.emplk.realestatemanager.domain.property_form.location.LocationFormEntity
import com.emplk.realestatemanager.domain.property_form.picture_preview.PicturePreviewEntity
import java.math.BigDecimal

data class PropertyFormEntity(
    val type: String? = null,
    val price: BigDecimal? = BigDecimal.ZERO,
    val surface: Int? = null,
    val rooms: Int? = null,
    val bedrooms: Int? = null,
    val bathrooms: Int? = null,
    val description: String? = null,
    val agentName: String? = null,
    val location: LocationFormEntity? = null,  // TODO: NOT SURE
    val pictures: List<PicturePreviewEntity> = emptyList(),
    val amenities: List<AmenityFormEntity> = emptyList(),
)
