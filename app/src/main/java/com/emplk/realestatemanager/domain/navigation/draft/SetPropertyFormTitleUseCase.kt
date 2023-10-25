package com.emplk.realestatemanager.domain.navigation.draft

import javax.inject.Inject

class SetPropertyFormTitleUseCase @Inject constructor(
    private val navigationDraftRepository: NavigationDraftRepository
) {
    fun invoke(title: String?) {
        navigationDraftRepository.setPropertyFormTitle(title)
    }
}
