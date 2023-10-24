package com.emplk.realestatemanager.domain.navigation.draft

import com.emplk.realestatemanager.domain.current_property.ResetCurrentPropertyIdUseCase
import javax.inject.Inject

class ClearPropertyFormNavigationUseCase @Inject constructor(
    private val navigationDraftRepository: NavigationDraftRepository,
    private val resetCurrentPropertyIdUseCase: ResetCurrentPropertyIdUseCase,
) {
    fun invoke() {
        navigationDraftRepository.clearPropertyDraftEvent()
        resetCurrentPropertyIdUseCase.invoke()
    }
}