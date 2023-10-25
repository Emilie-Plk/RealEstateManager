package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.data.property_draft.FormWithTitleAndLastEditionDate
import javax.inject.Inject

class GetAllDraftsWithTitleAndDateUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
) {
    suspend fun invoke(): List<FormWithTitleAndLastEditionDate> = formDraftRepository.getAllDrafts()
}