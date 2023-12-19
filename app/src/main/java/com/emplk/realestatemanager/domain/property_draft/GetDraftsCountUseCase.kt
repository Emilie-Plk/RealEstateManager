package com.emplk.realestatemanager.domain.property_draft

import javax.inject.Inject

class GetDraftsCountUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
) {
    suspend fun invoke(): Int = formDraftRepository.getFormsCount()
}