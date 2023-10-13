package com.emplk.realestatemanager.domain.property_draft.amenity

import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity

interface AmenityFormRepository {
    suspend fun add(amenityEntity: AmenityEntity, propertyFormId: Long): Long?
    suspend fun addAll(amenityEntities: List<AmenityEntity>, propertyFormId: Long): List<Long?>
    fun getAllAsFlow(): kotlinx.coroutines.flow.Flow<List<AmenityEntity>>
    suspend fun delete(amenityFormId: Long): Int
}
