package com.emplk.realestatemanager.domain.property.location

import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getAllPropertyLatLongAsFlow(): Flow<List<PropertyLatLongEntity>>
}