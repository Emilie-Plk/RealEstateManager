package com.emplk.realestatemanager.ui.add.draft_dialog

import androidx.lifecycle.ViewModel
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.navigation.draft.ClearPropertyFormNavigationUseCase
import com.emplk.realestatemanager.domain.navigation.draft.SaveDraftNavigationUseCase
import com.emplk.realestatemanager.domain.property_draft.SetFormTitleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SaveDraftDialogViewModel @Inject constructor(
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val saveDraftNavigationUseCase: SaveDraftNavigationUseCase,
    private val setFormTitleUseCase: SetFormTitleUseCase,
    private val clearPropertyFormNavigationUseCase: ClearPropertyFormNavigationUseCase,
) : ViewModel() {

    fun onAddDraftClicked() {
        saveDraftNavigationUseCase.invoke()
        setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
    }

    fun onCancelClicked() {
        clearPropertyFormNavigationUseCase.invoke()
        setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
    }

    fun setFormTitle(title: String) {
        setFormTitleUseCase.invoke(title)
    }
}


