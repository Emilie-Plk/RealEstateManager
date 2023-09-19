package com.emplk.realestatemanager.domain.property_form

import java.math.BigDecimal

data class PropertyFormEntity(
    val id: Long = 0,
    val type: String? = null,
    val price: BigDecimal? = BigDecimal.ZERO,
    val surface: String? = null,
    val rooms: String? = null,
    val bedrooms: String? = null,
    val bathrooms: String? = null,
    val description: String? = null,
    val agentName: String? = null,
)
