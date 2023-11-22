package com.emplk.realestatemanager.domain.navigation.draft

import com.emplk.realestatemanager.domain.current_property.ResetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.property_draft.ResetFormParamsUseCase
import javax.inject.Inject

class ClearPropertyFormNavigationUseCase @Inject constructor(
    private val formRepository: FormRepository,
    private val resetFormParamsUseCase: ResetFormParamsUseCase,
    private val resetCurrentPropertyIdUseCase: ResetCurrentPropertyIdUseCase,
) {
    fun invoke() {
        formRepository.clearPropertyDraftEvent()
        resetFormParamsUseCase.invoke()
        resetCurrentPropertyIdUseCase.invoke()
    }
}