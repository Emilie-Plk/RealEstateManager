package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.property_draft.model.FormWithDetailEntity
import javax.inject.Inject

class GetAllDraftsWithTitleAndDateUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
) {
    suspend fun invoke(): List<FormWithDetailEntity> = formDraftRepository.getFormsWithDetails()
}