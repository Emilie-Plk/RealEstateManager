package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.data.property_draft.FormTypeAndTitleEntity
import com.emplk.realestatemanager.domain.navigation.draft.FormRepository
import com.emplk.realestatemanager.ui.add.FormType
import javax.inject.Inject

class SetFormTitleUseCase @Inject constructor(
    private val formRepository: FormRepository
) {
    fun invoke(formType: FormType, title: String?) {
        formRepository.setFormTypeAndTitle(FormTypeAndTitleEntity(formType, title))
    }
}