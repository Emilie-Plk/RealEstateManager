package com.emplk.realestatemanager.domain.current_property

import kotlinx.coroutines.flow.Flow

interface CurrentPropertyRepository {
    fun getCurrentPropertyIdAsFlow(): Flow<Long?>
    fun setCurrentPropertyId(id: Long)
    fun resetCurrentPropertyId()
}
