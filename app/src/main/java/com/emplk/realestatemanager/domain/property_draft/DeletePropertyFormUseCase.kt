package com.emplk.realestatemanager.domain.property_draft

import javax.inject.Inject

class DeletePropertyFormUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
) {
    suspend fun invoke(id: Long): Boolean =
        formDraftRepository.delete(id)
}