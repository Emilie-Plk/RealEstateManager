package com.emplk.realestatemanager.domain.navigation.draft

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsFormCompletedAsFlowUseCase @Inject constructor(private val formRepository: FormRepository) {
    fun invoke(): Flow<Boolean?> = formRepository.isPropertyFormCompletedAsFlow()
}