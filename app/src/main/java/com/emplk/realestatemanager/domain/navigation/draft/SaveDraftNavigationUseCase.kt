package com.emplk.realestatemanager.domain.navigation.draft

import com.emplk.realestatemanager.domain.current_property.ResetCurrentPropertyIdUseCase
import javax.inject.Inject

class SaveDraftNavigationUseCase @Inject constructor(
    private val formRepository: FormRepository,
    private val resetCurrentPropertyIdUseCase: ResetCurrentPropertyIdUseCase,
) {
    fun invoke() {
        formRepository.savePropertyDraftEvent()
        resetCurrentPropertyIdUseCase.invoke()
    }
}