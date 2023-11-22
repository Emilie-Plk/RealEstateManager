package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.navigation.draft.FormRepository
import javax.inject.Inject

class SetPropertyFormProgressUseCase @Inject constructor(
    private val formRepository: FormRepository,
) {
    fun invoke(isPropertyFormInProgress: Boolean) =
        formRepository.setPropertyFormProgress(isPropertyFormInProgress)
}