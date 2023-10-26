package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.ui.add.FormType

sealed class FormState {
    data class EmptyForm(val formDraftEntity: FormDraftEntity, val formType: FormType) : FormState()
    data class Draft(val formDraftEntity: FormDraftEntity, val formType: FormType) : FormState()
}
