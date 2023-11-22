package com.emplk.realestatemanager.domain.navigation.draft

import javax.inject.Inject

class SetFormCompletionUseCase @Inject constructor(private val formRepository: FormRepository) {
    fun invoke(isCompleted: Boolean) = formRepository.setPropertyFormCompletion(isCompleted)
}