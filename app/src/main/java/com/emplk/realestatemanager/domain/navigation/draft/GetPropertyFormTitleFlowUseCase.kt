package com.emplk.realestatemanager.domain.navigation.draft

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPropertyFormTitleFlowUseCase @Inject constructor(
    private val navigationDraftRepository: NavigationDraftRepository,
) {
    fun invoke() : Flow<String?> = navigationDraftRepository.getPropertyFormTitle()
}