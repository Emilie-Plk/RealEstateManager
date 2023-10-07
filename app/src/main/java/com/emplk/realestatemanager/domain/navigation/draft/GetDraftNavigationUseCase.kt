package com.emplk.realestatemanager.domain.navigation.draft

import javax.inject.Inject

class GetDraftNavigationUseCase @Inject constructor(private val navigationDraftRepository: NavigationDraftRepository) {
    fun invoke() = navigationDraftRepository.getSavedPropertyDraftEvent()
}