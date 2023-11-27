package com.emplk.realestatemanager.domain.property_draft

import android.content.res.Resources
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.data.property_draft.FormTypeAndTitleEntity
import com.emplk.realestatemanager.domain.navigation.draft.FormRepository
import com.emplk.realestatemanager.ui.add.FormType
import javax.inject.Inject

class SetFormTitleUseCase @Inject constructor(
    private val formRepository: FormRepository,
    private val resources: Resources,
) {
    fun invoke(formType: FormType, title: String?) {
        formRepository.setFormTypeAndTitle(
            FormTypeAndTitleEntity(
                formType,
                title
            )
        )
    }
}