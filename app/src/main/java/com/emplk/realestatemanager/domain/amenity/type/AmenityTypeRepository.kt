package com.emplk.realestatemanager.domain.amenity.type

import com.emplk.realestatemanager.domain.amenity.AmenityType
import kotlinx.coroutines.flow.Flow

interface AmenityTypeRepository {
    fun getAmenityTypesAsFlow(): Flow<List<AmenityType>>
}
