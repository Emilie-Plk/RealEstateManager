package com.emplk.realestatemanager.domain.property_draft.model

import com.emplk.realestatemanager.ui.add.FormType

data class FormTypeAndTitleEntity(
    val formType: FormType,
    val title: String?
)