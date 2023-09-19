package com.emplk.realestatemanager.domain.property_form.location

import com.emplk.realestatemanager.data.property_form.location.LocationFormDto

interface LocationFormRepository {
    suspend fun add(locationFormEntity: LocationFormEntity, propertyFormId: Long): Long

    suspend fun update(locationFormEntity: LocationFormEntity, propertyFormId: Long): Int

    suspend fun delete(locationFormId: Long): Int
}
