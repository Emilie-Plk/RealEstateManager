package com.emplk.realestatemanager.domain.amenity

import com.emplk.realestatemanager.data.amenity.AmenityDtoEntity

interface AmenityRepository {

    suspend fun addAmenity(amenityEntity: AmenityEntity)

    suspend fun updateAmenity(amenityEntity: AmenityEntity)
}
