package com.emplk.realestatemanager.domain.navigation.draft

import android.util.Log
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavePropertyDraftEvent @Inject constructor(private val formRepository: FormRepository) {
    fun invoke(): Flow<Unit> {
        Log.d("COUCOU", "GetSavePropertyDraftEvent.invoke")
        return formRepository.getSavePropertyDraftEvent()
    }
}