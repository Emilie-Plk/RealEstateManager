package com.emplk.realestatemanager.domain.property_form.amenity

interface AmenityFormRepository {

    suspend fun add(amenityFormEntity: AmenityFormEntity, propertyFormId: Long): Long

    suspend fun delete(amenityFormId: Long): Int
}
