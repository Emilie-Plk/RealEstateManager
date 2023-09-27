package com.emplk.realestatemanager.domain.property.amenity.type

import com.emplk.realestatemanager.domain.property.amenity.AmenityType

interface AmenityTypeRepository {
    fun getAmenityTypes(): List<AmenityType>
}
