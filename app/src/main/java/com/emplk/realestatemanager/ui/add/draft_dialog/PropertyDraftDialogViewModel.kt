package com.emplk.realestatemanager.ui.add.draft_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property_form.DeleteTemporaryPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_form.SavePropertyFormEventUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.id.DeleteAllPicturePreviewIdsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyDraftDialogViewModel @Inject constructor(
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val deleteTemporaryPropertyFormUseCase: DeleteTemporaryPropertyFormUseCase,
    private val deleteAllPicturePreviewIdsUseCase: DeleteAllPicturePreviewIdsUseCase,
    private val savePropertyFormEventUseCase: SavePropertyFormEventUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    fun onAddDraftClicked() {
        savePropertyFormEventUseCase.invoke()
        setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
    }

    fun onCancelClicked() {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            deleteTemporaryPropertyFormUseCase.invoke()
        }  // TODO: working but... valid?
        viewModelScope.launch(coroutineDispatcherProvider.main) {
            deleteAllPicturePreviewIdsUseCase.invoke()
            setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
        }
    }
}

