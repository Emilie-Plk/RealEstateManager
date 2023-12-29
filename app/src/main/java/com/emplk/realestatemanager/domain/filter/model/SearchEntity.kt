package com.emplk.realestatemanager.domain.filter.model

import java.math.BigDecimal

data class SearchEntity(
    val propertyType: String?,
    val minPrice: BigDecimal,
    val maxPrice: BigDecimal,
    val minSurface: BigDecimal,
    val maxSurface: BigDecimal,
    val amenitySchool: Boolean?,
    val amenityPark: Boolean?,
    val amenityShopping: Boolean?,
    val amenityRestaurant: Boolean?,
    val amenityConcierge: Boolean?,
    val amenityGym: Boolean?,
    val amenityTransport: Boolean?,
    val amenityHospital: Boolean?,
    val amenityLibrary: Boolean?,
    val entryDateEpochMin: Long?,
    val entryDateEpochMax: Long?,
    val isSold: Boolean?
)