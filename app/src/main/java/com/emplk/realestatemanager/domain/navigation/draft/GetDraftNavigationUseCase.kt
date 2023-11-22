package com.emplk.realestatemanager.domain.navigation.draft

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDraftNavigationUseCase @Inject constructor(private val formRepository: FormRepository) {
    fun invoke(): Flow<Unit> = formRepository.getSavedPropertyDraftEvent()
}