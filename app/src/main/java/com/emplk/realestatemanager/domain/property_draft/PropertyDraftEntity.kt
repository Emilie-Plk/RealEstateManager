package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.property_draft.picture_preview.PicturePreviewEntity
import java.math.BigDecimal

data class PropertyDraftEntity(
    val type: String?,
    val price: BigDecimal,
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