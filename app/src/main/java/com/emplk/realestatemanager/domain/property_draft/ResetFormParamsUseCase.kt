package com.emplk.realestatemanager.domain.property_draft

import android.util.Log
import com.emplk.realestatemanager.data.navigation.FormRepositoryImpl
import javax.inject.Inject

class ResetFormParamsUseCase @Inject constructor(
    private val formRepository: FormRepositoryImpl
) {
    fun invoke() {
        formRepository.resetFormTypeAndTitle()
        formRepository.resetPropertyFormCompletion()
        formRepository.resetPropertyFormProgress()
    }
}