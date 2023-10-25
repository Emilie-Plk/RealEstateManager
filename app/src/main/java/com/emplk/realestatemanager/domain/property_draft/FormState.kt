package com.emplk.realestatemanager.domain.property_draft

sealed class FormState {
    data class EmptyForm(val newPropertyFormId: Long) : FormState()
    data class Draft(val formDraftEntity: FormDraftEntity) : FormState()
}
