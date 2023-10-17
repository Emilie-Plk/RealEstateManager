package com.emplk.realestatemanager.domain.property_draft

import javax.inject.Inject

class GetCurrentDraftIdUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository
) {
    suspend fun invoke(): Long? = formDraftRepository.getExistingPropertyFormId()
}