package com.emplk.realestatemanager.domain.property.location

import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getAllPropertyLatLongAndSoldStatusAsFlow(): Flow<List<PropertyLatLongAndSoldStatusEntity>>
}