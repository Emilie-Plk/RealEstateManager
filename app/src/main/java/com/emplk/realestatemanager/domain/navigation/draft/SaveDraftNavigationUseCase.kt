package com.emplk.realestatemanager.domain.navigation.draft

import android.util.Log
import com.emplk.realestatemanager.domain.current_property.ResetCurrentPropertyIdUseCase
import javax.inject.Inject

class SaveDraftNavigationUseCase @Inject constructor(
    private val formRepository: FormRepository,
    private val resetCurrentPropertyIdUseCase: ResetCurrentPropertyIdUseCase,
) {
    fun invoke() {
        Log.d("COUCOU", "SaveDraftNavigationUseCase.invoke")
        formRepository.savePropertyDraftEvent()
        resetCurrentPropertyIdUseCase.invoke()
    }
}