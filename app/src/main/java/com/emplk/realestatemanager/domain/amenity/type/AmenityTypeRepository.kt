package com.emplk.realestatemanager.domain.amenity.type

import com.emplk.realestatemanager.domain.amenity.AmenityType

interface AmenityTypeRepository {
    fun getAmenityTypes(): List<AmenityType>
}
