package com.emplk.realestatemanager.domain.amenity

data class AmenityEntity(
    val id: Long,
    val type: AmenityType,
    val propertyId: Long,
)
