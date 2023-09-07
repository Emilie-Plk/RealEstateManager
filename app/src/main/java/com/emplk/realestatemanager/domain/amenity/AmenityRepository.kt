package com.emplk.realestatemanager.domain.amenity

interface AmenityRepository {

    suspend fun addAmenity(amenityEntity: AmenityEntity)

    suspend fun updateAmenity(amenityEntity: AmenityEntity)
}
