package com.emplk.realestatemanager.domain.property_draft.amenity

import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity
import kotlinx.coroutines.flow.Flow

interface AmenityFormRepository {

    suspend fun add(amenityEntity: AmenityEntity, propertyFormId: Long): Long?

    suspend fun addAll(amenityEntities: List<AmenityEntity>, propertyFormId: Long): List<Long?>

    fun getAllAsFlow(): Flow<List<AmenityEntity>>

    suspend fun delete(amenityFormId: Long): Int
}
