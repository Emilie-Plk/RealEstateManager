package com.emplk.realestatemanager.ui.drafts.add_form_dialog

import androidx.lifecycle.ViewModel
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.navigation.draft.SaveDraftNavigationUseCase
import com.emplk.realestatemanager.domain.navigation.draft.SetPropertyFormTitleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FormTitleDialogFragmentViewModel @Inject constructor(
    private val setPropertyFormTitleUseCase: SetPropertyFormTitleUseCase,
    private val saveDraftNavigationUseCase: SaveDraftNavigationUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
) : ViewModel() {

    fun getEnteredTitle(title: String) {
        setPropertyFormTitleUseCase.invoke(title)
        saveDraftNavigationUseCase.invoke()
        setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
    }
}