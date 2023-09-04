package com.emplk.realestatemanager.domain.property

import com.emplk.realestatemanager.domain.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.location.LocationEntity
import com.emplk.realestatemanager.domain.pictures.PictureEntity
import java.math.BigDecimal
import java.time.LocalDateTime

data class PropertyEntity(
    val id: Long,
    val type: String,
    val price: BigDecimal,
    val surface: Int,
    val location: LocationEntity,
    val rooms: Int,
    val bedrooms: Int,
    val bathrooms: Int,
    val pictures: List<PictureEntity>,
    val amenities: List<AmenityEntity>,
    val description: String,
    val agentName: String,
    val isAvailableForSale: Boolean,
    val isSold: Boolean,
    val entryDate: LocalDateTime,
    val saleDate: LocalDateTime?,
)
