package com.emplk.realestatemanager.domain.navigation.draft

import com.emplk.realestatemanager.domain.current_property.ResetCurrentPropertyIdUseCase
import javax.inject.Inject

class SaveDraftNavigationUseCase @Inject constructor(
    private val navigationDraftRepository: NavigationDraftRepository,
    private val resetCurrentPropertyIdUseCase: ResetCurrentPropertyIdUseCase,
) {
    fun invoke() {
        navigationDraftRepository.savePropertyDraftEvent()
        resetCurrentPropertyIdUseCase.invoke()
    }
}