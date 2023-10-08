package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.navigation.draft.NavigationDraftRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsPropertyFormInProgressUseCase @Inject constructor(private val navigationDraftRepository: NavigationDraftRepository) {

    fun invoke(): Flow<Boolean> =
        navigationDraftRepository.isPropertyFormInProgressAsFlow()
}