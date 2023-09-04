package com.emplk.realestatemanager.domain.location

data class LocationEntity(
    val id: Long,
    val propertyId: Long,
    val address: String,
    val city: String,
    val postalCode: String,
    val latitude: Double,
    val longitude: Double,
)