package com.emplk.realestatemanager.ui.add.draft_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.navigation.draft.SaveDraftNavigationUseCase
import com.emplk.realestatemanager.domain.property_draft.ClearPropertyFormUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyDraftDialogViewModel @Inject constructor(
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val clearPropertyFormUseCase: ClearPropertyFormUseCase,
    private val saveDraftNavigationUseCase: SaveDraftNavigationUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    fun onAddDraftClicked() {
        saveDraftNavigationUseCase.invoke()
        setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
    }

    fun onCancelClicked() {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            clearPropertyFormUseCase.invoke()
        }
        setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
    }
}


