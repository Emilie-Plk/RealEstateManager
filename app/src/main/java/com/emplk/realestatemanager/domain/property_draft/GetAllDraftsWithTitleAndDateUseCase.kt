package com.emplk.realestatemanager.domain.property_draft

import javax.inject.Inject

class GetAllDraftsWithTitleAndDateUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
) {
    suspend fun invoke(): List<FormWithTitleDateAndFeaturedPictureEntity> =
        formDraftRepository.getDraftsWithFeaturePicture()
}