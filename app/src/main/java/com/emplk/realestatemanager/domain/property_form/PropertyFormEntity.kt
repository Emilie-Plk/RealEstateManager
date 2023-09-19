package com.emplk.realestatemanager.domain.property_form

data class PropertyFormEntity(
    val id: Long = 0,
    val type: String,
    val price: String,
    val surface: String,
    val rooms: String,
    val bedrooms: String,
    val bathrooms: String,
    val description: String,
    val agentName: String,
)
