package com.emplk.realestatemanager.domain.amenity

import com.emplk.realestatemanager.data.amenity.AmenityDtoEntity

interface AmenityRepository {

    suspend fun addAmenity(amenityDtoEntity: AmenityDtoEntity)

    suspend fun updateAmenity(amenityDtoEntity: AmenityDtoEntity)
}
