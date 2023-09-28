package com.emplk.realestatemanager.ui.add.draft_dialog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property_form.DeleteTemporaryPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_form.GetCurrentPropertyFormIdUseCase
import com.emplk.realestatemanager.domain.property_form.OnSavePropertyFormEventUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.id.DeleteAllPicturePreviewIdsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyDraftDialogViewModel @Inject constructor(
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val deleteTemporaryPropertyFormUseCase: DeleteTemporaryPropertyFormUseCase,
    private val getCurrentPropertyFormIdUseCase: GetCurrentPropertyFormIdUseCase,
    private val deleteAllPicturePreviewIdsUseCase: DeleteAllPicturePreviewIdsUseCase,
    private val onSavePropertyFormEventUseCase: OnSavePropertyFormEventUseCase,
) : ViewModel() {
    fun onAddDraftClicked() {
        onSavePropertyFormEventUseCase.invoke()
        setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
    }

    fun onCancelClicked() {
        viewModelScope.launch {
            deleteTemporaryPropertyFormUseCase.invoke()
        }
        deleteAllPicturePreviewIdsUseCase.invoke()
        setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
    }
}