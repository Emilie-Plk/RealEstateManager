package com.emplk.realestatemanager.domain.property_draft

sealed class PropertyFormDatabaseState {
    data class Empty(val newPropertyFormId: Long) : PropertyFormDatabaseState()
    data class DraftAlreadyExists(val formDraftEntity: FormDraftEntity) : PropertyFormDatabaseState()
}
