package com.emplk.realestatemanager.domain.property_form.amenity

import kotlinx.coroutines.flow.Flow

interface AmenityFormRepository {

    suspend fun add(amenityFormEntity: AmenityFormEntity, propertyFormId: Long): Long?

    suspend fun addAll(amenityFormEntities: List<AmenityFormEntity>, propertyFormId: Long): List<Long?>

    fun getAllAsFlow(): Flow<List<AmenityFormEntity>>

    suspend fun delete(amenityFormId: Long): Int
}
