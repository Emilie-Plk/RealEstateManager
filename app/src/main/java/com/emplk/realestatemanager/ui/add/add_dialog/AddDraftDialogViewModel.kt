package com.emplk.realestatemanager.ui.add.add_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property_form.DeleteTemporaryPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_form.GetCurrentPropertyFormIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDraftDialogViewModel @Inject constructor(
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val deleteTemporaryPropertyFormUseCase: DeleteTemporaryPropertyFormUseCase,
    private val getCurrentPropertyFormIdUseCase: GetCurrentPropertyFormIdUseCase,
) : ViewModel() {
    fun onAddDraftClicked() {
        // Save form in db
        setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
    }

    fun onCancelClicked() {
        viewModelScope.launch {
            val propertyFormId = async {
                getCurrentPropertyFormIdUseCase.invoke()
            }.await()

            deleteTemporaryPropertyFormUseCase.invoke(propertyFormId)
        }
        setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
    }
}