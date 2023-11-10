package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.domain.property_draft.picture_preview.PicturePreviewEntity
import java.math.BigDecimal
import java.time.LocalDateTime

data class FormDraftEntity(
    val id: Long,
    val type: String?,
    val title: String?,
    val price: BigDecimal,
    val surface: BigDecimal,
    val address: String?,
    val isAddressValid: Boolean,
    val rooms: Int?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val description: String?,
    val agentName: String?,
    val pictures: List<PicturePreviewEntity> = emptyList(),
    val amenities: List<AmenityType> = emptyList(),
    val isSold: Boolean,
    val entryDate: LocalDateTime?,
    val entryDateEpoch: Long?,
    val saleDate: LocalDateTime?,
    val lastEditionDate: LocalDateTime?,
)
