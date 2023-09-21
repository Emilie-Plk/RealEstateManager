package com.emplk.realestatemanager.domain.property_form

import com.emplk.realestatemanager.domain.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.location.LocationEntity
import com.emplk.realestatemanager.domain.pictures.PictureEntity
import com.emplk.realestatemanager.domain.property_form.amenity.AmenityFormEntity
import com.emplk.realestatemanager.domain.property_form.location.LocationFormEntity
import com.emplk.realestatemanager.domain.property_form.picture_preview.PicturePreviewEntity
import java.math.BigDecimal

data class PropertyFormEntity(
    val id: Long = 0,
    val type: String?,
    val price: BigDecimal? = BigDecimal.ZERO,
    val surface: Int?,
    val rooms: Int?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val description: String?,
    val agentName: String?,
    val location: LocationFormEntity?,
    val pictures: List<PicturePreviewEntity>,
    val amenities: List<AmenityFormEntity>,
)
