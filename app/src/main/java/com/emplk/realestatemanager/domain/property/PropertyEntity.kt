package com.emplk.realestatemanager.domain.property

import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.domain.property.location.LocationEntity
import com.emplk.realestatemanager.domain.property.pictures.PictureEntity
import java.math.BigDecimal
import java.time.LocalDateTime

data class PropertyEntity(
    val id: Long = 0,
    val type: String,
    val price: BigDecimal,
    val surface: BigDecimal,
    val location: LocationEntity,
    val rooms: Int,
    val bedrooms: Int,
    val bathrooms: Int,
    val pictures: List<PictureEntity>,
    val amenities: List<AmenityType>,
    val description: String,
    val agentName: String,
    val isSold: Boolean,
    val entryDate: LocalDateTime,
    val entryDateEpoch: Long,
    val lastEditionDate: LocalDateTime?,
    val saleDate: LocalDateTime?,
)
