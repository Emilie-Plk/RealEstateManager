package com.emplk.realestatemanager.ui.add.save_draft

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.navigation.draft.ClearPropertyFormNavigationUseCase
import com.emplk.realestatemanager.domain.navigation.draft.SaveDraftNavigationUseCase
import com.emplk.realestatemanager.domain.property_draft.ClearPropertyFormProgressUseCase
import com.emplk.realestatemanager.domain.property_draft.GetFormTitleAsFlowUseCase
import com.emplk.realestatemanager.domain.property_draft.SetFormTitleUseCase
import com.emplk.realestatemanager.ui.add.FormType
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class SaveDraftDialogViewModel @Inject constructor(
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val saveDraftNavigationUseCase: SaveDraftNavigationUseCase,
    private val setFormTitleUseCase: SetFormTitleUseCase,
    private val getFormTitleAsFlowUseCase: GetFormTitleAsFlowUseCase,
    private val clearPropertyFormProgressUseCase: ClearPropertyFormProgressUseCase,
    private val clearPropertyFormNavigationUseCase: ClearPropertyFormNavigationUseCase,
) : ViewModel() {

    private val isTitleMissingMutableStateFlow: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    private val hasSaveButtonBeingClicked: MutableStateFlow<Boolean?> =
        MutableStateFlow(null)
    val viewState: LiveData<SaveDraftViewState> = liveData {
        combine(
            getFormTitleAsFlowUseCase.invoke(),
            isTitleMissingMutableStateFlow,
            hasSaveButtonBeingClicked
        ) { formTypeAndTitle, isTitleMissing, hasSaveButtonClicked ->
            emit(
                SaveDraftViewState(
                    isSaveButtonVisible = hasSaveButtonClicked == null || hasSaveButtonClicked == false,
                    saveButtonEvent = EquatableCallback {
                        hasSaveButtonBeingClicked.tryEmit(true)
                        if (formTypeAndTitle.formType == FormType.EDIT || (formTypeAndTitle.formType == FormType.ADD && formTypeAndTitle.title != null)) {
                            isTitleMissingMutableStateFlow.tryEmit(false)
                            saveDraftNavigationUseCase.invoke()
                            clearPropertyFormProgressUseCase.invoke()
                            setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                        } else {
                            isTitleMissingMutableStateFlow.tryEmit(true)
                        }
                    },
                    isSubmitTitleButtonVisible = hasSaveButtonClicked == true && isTitleMissing == true,
                    submitTitleEvent = EquatableCallbackWithParam { title ->
                        setFormTitleUseCase.invoke(formTypeAndTitle.formType, title)
                        clearPropertyFormProgressUseCase.invoke()
                        setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                    },
                    discardEvent = EquatableCallback {
                        clearPropertyFormNavigationUseCase.invoke()
                        setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                    },
                    isTitleTextInputVisible = hasSaveButtonClicked == true && isTitleMissing == true,
                )
            )
        }.collect()
    }
}


