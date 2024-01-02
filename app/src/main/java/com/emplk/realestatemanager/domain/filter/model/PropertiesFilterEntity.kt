package com.emplk.realestatemanager.domain.filter.model

import com.emplk.realestatemanager.domain.filter.SearchedEntryDateRange
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.ui.filter.PropertySaleState
import java.math.BigDecimal

data class PropertiesFilterEntity(
    val propertyType: String? = null,
    val minMaxPrice: Pair<BigDecimal, BigDecimal> = Pair(BigDecimal.ZERO, BigDecimal.ZERO),
    val minMaxSurface: Pair<BigDecimal, BigDecimal> = Pair(BigDecimal.ZERO, BigDecimal.ZERO),
    val entryDate: SearchedEntryDateRange = SearchedEntryDateRange.ALL,
    val availableForSale: PropertySaleState = PropertySaleState.ALL,
    val amenities: List<AmenityType> = emptyList(),
)
