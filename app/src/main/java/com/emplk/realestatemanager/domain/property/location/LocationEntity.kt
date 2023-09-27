package com.emplk.realestatemanager.domain.property.location

data class LocationEntity(
    val id: Long = 0,
    val address: String,
    val miniatureMapPath: String,
    val latitude: String,
    val longitude: String,
)