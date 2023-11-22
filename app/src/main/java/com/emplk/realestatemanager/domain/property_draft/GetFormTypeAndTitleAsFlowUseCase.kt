package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.data.property_draft.FormTypeAndTitleEntity
import com.emplk.realestatemanager.domain.navigation.draft.FormRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class GetFormTypeAndTitleAsFlowUseCase @Inject constructor(
    private val formRepository: FormRepository,
) {
    fun invoke(): Flow<FormTypeAndTitleEntity> {
        return formRepository.getFormTypeAndTitleAsFlow()
    }
}