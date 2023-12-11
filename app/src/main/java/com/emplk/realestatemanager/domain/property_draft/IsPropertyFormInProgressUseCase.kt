package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.navigation.draft.FormRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class IsPropertyFormInProgressUseCase @Inject constructor(private val formRepository: FormRepository) {

    fun invoke(): Flow<Boolean> =
        formRepository.isPropertyFormInProgressAsFlow().filterNotNull()
}