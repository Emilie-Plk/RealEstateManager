package com.emplk.realestatemanager.data.property

import androidx.room.ColumnInfo
import java.math.BigDecimal

data class PropertyTypeSurfacePriceAndPictureDto(
    val id: Long,
    val type: String,
    val price: BigDecimal,
    val surface: Double,
    @ColumnInfo(name = "uri")
    val pictureUri: String,
    val rooms: Int,
    val bedrooms: Int,
    val bathrooms: Int,
    val description: String,
)