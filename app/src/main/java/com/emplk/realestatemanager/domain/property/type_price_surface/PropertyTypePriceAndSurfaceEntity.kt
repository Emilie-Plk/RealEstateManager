package com.emplk.realestatemanager.domain.property.type_price_surface

import java.math.BigDecimal

data class PropertyTypePriceAndSurfaceEntity(
    val id: Long,
    val type: String,
    val price: BigDecimal,
    val surface: Int,
    val featuredPictureUri: String,
    val pictureUri: String,
    //   val amenities: List<AmenityEntity>,
    val rooms: Int,
    val bedrooms: Int,
    val bathrooms: Int,
    val description: String,
)