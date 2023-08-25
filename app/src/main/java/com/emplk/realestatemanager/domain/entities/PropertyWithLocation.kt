package com.emplk.realestatemanager.domain.entities

data class PropertyWithLocation(
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val city: String,
    val neighborhood: String,
    val postalCode: String,
)