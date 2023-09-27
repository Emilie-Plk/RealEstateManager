package com.emplk.realestatemanager.domain.location

data class LocationEntity(
    val id: Long = 0,
    val propertyId: Long,
    val address: String,
    val miniatureMapPath: String,
    val latitude: String,
    val longitude: String,
)