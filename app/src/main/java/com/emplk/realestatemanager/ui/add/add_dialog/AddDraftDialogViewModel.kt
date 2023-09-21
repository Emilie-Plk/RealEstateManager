package com.emplk.realestatemanager.ui.add.add_dialog

import androidx.lifecycle.ViewModel
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddDraftDialogViewModel @Inject constructor(
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
) : ViewModel() {
    fun onAddDraftClicked() {
        // Save form in db
        setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
    }

    fun onCancelClicked() {
        // Clear form db
        setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
    }
}