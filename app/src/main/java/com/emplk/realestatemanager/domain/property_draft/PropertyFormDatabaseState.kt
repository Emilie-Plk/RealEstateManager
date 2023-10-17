package com.emplk.realestatemanager.domain.property_draft

sealed class PropertyFormDatabaseState {
    data class EmptyForm(val newPropertyFormId: Long) : PropertyFormDatabaseState()
    data class Draft(val formDraftEntity: FormDraftEntity) : PropertyFormDatabaseState()
}
    