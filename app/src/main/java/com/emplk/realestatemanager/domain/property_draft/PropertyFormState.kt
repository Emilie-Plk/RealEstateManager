package com.emplk.realestatemanager.domain.property_draft

sealed class PropertyFormState {
    data class EmptyForm(val newPropertyFormId: Long) : PropertyFormState()
    data class Draft(val formDraftEntity: FormDraftEntity) : PropertyFormState()
}
