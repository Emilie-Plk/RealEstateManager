package com.emplk.realestatemanager.domain.navigation.draft

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClearPropertyFormNavigationEventUseCase @Inject constructor(
    private val navigationDraftRepository: NavigationDraftRepository
) {
    fun invoke(): Flow<Unit> = navigationDraftRepository.getClearedPropertyDraftEvent()
}
