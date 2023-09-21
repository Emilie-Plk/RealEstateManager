package com.emplk.realestatemanager.domain.property_form.amenity

import kotlinx.coroutines.flow.Flow

interface AmenityFormRepository {

    suspend fun add(amenityFormEntity: AmenityFormEntity, propertyFormId: Long): Long?

    fun getAllAsFlow(): Flow<List<AmenityFormEntity>>

    suspend fun delete(amenityFormId: Long): Int
}
