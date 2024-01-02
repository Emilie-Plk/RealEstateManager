package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.navigation.draft.FormRepository
import com.emplk.realestatemanager.domain.property_draft.model.FormTypeAndTitleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class GetFormTypeAndTitleAsFlowUseCase @Inject constructor(
    private val formRepository: FormRepository,
) {
    fun invoke(): Flow<FormTypeAndTitleEntity> = formRepository.getFormTypeAndTitleAsFlow().filterNotNull()
}