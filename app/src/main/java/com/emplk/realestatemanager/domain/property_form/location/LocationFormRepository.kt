package com.emplk.realestatemanager.domain.property_form.location

interface LocationFormRepository {
    suspend fun add(locationFormEntity: LocationFormEntity, propertyFormId: Long): Long?

    suspend fun update(locationFormEntity: LocationFormEntity, propertyFormId: Long): Int

    suspend fun delete(locationFormId: Long) : Int?
}
