package com.emplk.realestatemanager.domain.amenity

interface AmenityRepository {

    suspend fun addAmenity(amenityEntity: AmenityEntity, propertyId: Long) : Boolean

    suspend fun updateAmenity(amenityEntity: AmenityEntity, propertyId: Long) : Boolean
}
