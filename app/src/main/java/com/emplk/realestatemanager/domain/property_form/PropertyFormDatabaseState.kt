package com.emplk.realestatemanager.domain.property_form

sealed class PropertyFormDatabaseState {
    data class Empty(val newPropertyFormId: Long) : PropertyFormDatabaseState()
    data class DraftAlreadyExists(val propertyFormEntity: PropertyFormEntity) : PropertyFormDatabaseState()
}
