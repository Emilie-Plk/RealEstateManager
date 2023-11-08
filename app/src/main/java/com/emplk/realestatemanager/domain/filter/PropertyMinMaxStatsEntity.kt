package com.emplk.realestatemanager.domain.filter

import androidx.room.ColumnInfo
import java.math.BigDecimal

data class PropertyMinMaxStatsEntity(
    val minPrice: BigDecimal,
    val maxPrice: BigDecimal,
    val minSurface: BigDecimal,
    val maxSurface: BigDecimal,
)