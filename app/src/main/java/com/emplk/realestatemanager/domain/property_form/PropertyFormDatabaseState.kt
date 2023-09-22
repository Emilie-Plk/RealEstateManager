package com.emplk.realestatemanager.domain.property_form

import kotlinx.coroutines.flow.Flow

sealed class PropertyFormDatabaseState {
    data class Empty(val newPropertyFormId: Long) : PropertyFormDatabaseState()
    data class DraftAlreadyExists(val propertyFormEntity: Flow<PropertyFormEntity>) : PropertyFormDatabaseState()
}
