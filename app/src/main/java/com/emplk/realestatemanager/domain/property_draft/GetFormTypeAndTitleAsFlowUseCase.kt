package com.emplk.realestatemanager.domain.property_draft

import android.util.Log
import com.emplk.realestatemanager.data.property_draft.FormTypeAndTitleEntity
import com.emplk.realestatemanager.domain.navigation.draft.FormRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class GetFormTypeAndTitleAsFlowUseCase @Inject constructor(
    private val formRepository: FormRepository,
) {
    fun invoke(): Flow<FormTypeAndTitleEntity> {
        Log.d("COUCOU", "GetFormTypeAndTitleAsFlowUseCase.invoke")
        return formRepository.getFormTypeAndTitleAsFlow().filterNotNull()
    }
}