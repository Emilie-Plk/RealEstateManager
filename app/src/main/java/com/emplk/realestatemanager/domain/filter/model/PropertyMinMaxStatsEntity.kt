package com.emplk.realestatemanager.domain.filter.model

import java.math.BigDecimal

data class PropertyMinMaxStatsEntity(
    val minPrice: BigDecimal,
    val maxPrice: BigDecimal,
    val minSurface: BigDecimal,
    val maxSurface: BigDecimal,
)