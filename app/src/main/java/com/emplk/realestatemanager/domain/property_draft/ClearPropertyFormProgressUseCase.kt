package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.navigation.draft.NavigationDraftRepository
import javax.inject.Inject

class ClearPropertyFormProgressUseCase @Inject constructor(
    private val navigationDraftRepository: NavigationDraftRepository,
) {
    fun invoke() {
        navigationDraftRepository.clearPropertyFormProgress()
    }
}