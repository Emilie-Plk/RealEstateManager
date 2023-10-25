package com.emplk.realestatemanager.ui.add.draft_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.domain.navigation.draft.GetPropertyFormTitleFlowUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.navigation.draft.ClearPropertyFormNavigationUseCase
import com.emplk.realestatemanager.domain.navigation.draft.SaveDraftNavigationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveDraftDialogViewModel @Inject constructor(
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val saveDraftNavigationUseCase: SaveDraftNavigationUseCase,
    private val getPropertyFormTitleFlowUseCase: GetPropertyFormTitleFlowUseCase,
    private val clearPropertyFormNavigationUseCase: ClearPropertyFormNavigationUseCase,
) : ViewModel() {

    fun onAddDraftClicked() {
        viewModelScope.launch {
            getPropertyFormTitleFlowUseCase.invoke().collect { hasTitle ->
                if (!hasTitle.isNullOrBlank()) {
                    saveDraftNavigationUseCase.invoke()
                    setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                } else {
                    setNavigationTypeUseCase.invoke(NavigationFragmentType.TITLE_DRAFT_DIALOG_FRAGMENT)
                }
            }
        }
    }

    fun onCancelClicked() {
        clearPropertyFormNavigationUseCase.invoke()
        setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
    }
}


