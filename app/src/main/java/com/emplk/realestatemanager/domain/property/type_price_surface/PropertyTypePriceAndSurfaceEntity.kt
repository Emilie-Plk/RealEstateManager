package com.emplk.realestatemanager.domain.property.type_price_surface

import java.math.BigDecimal

data class PropertyTypePriceAndSurfaceEntity(
    val id: Long,
    val type: String,
    val price: BigDecimal,
    val surface: Int,
    val featuredPictureUri: String,
)