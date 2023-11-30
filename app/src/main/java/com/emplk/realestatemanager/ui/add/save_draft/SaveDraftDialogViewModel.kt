package com.emplk.realestatemanager.ui.add.save_draft

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.navigation.draft.ClearPropertyFormNavigationUseCase
import com.emplk.realestatemanager.domain.navigation.draft.SaveDraftNavigationUseCase
import com.emplk.realestatemanager.domain.property_draft.GetFormTypeAndTitleAsFlowUseCase
import com.emplk.realestatemanager.domain.property_draft.ResetFormParamsUseCase
import com.emplk.realestatemanager.domain.property_draft.SetFormTitleUseCase
import com.emplk.realestatemanager.ui.add.FormType
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class SaveDraftDialogViewModel @Inject constructor(
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val saveDraftNavigationUseCase: SaveDraftNavigationUseCase,
    private val setFormTitleUseCase: SetFormTitleUseCase,
    private val getFormTypeAndTitleAsFlowUseCase: GetFormTypeAndTitleAsFlowUseCase,
    private val resetFormParamsUseCase: ResetFormParamsUseCase,
    private val clearPropertyFormNavigationUseCase: ClearPropertyFormNavigationUseCase,
) : ViewModel() {

    private val hasSaveButtonBeingClicked: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    val viewState: LiveData<SaveDraftViewState> = liveData {
        combine(
            getFormTypeAndTitleAsFlowUseCase.invoke(),
            hasSaveButtonBeingClicked
        ) { formTypeAndTitle, hasSaveButtonClicked ->
            SaveDraftViewState(
                isSaveMessageVisible = hasSaveButtonClicked == null || hasSaveButtonClicked == false,
                saveButtonEvent = EquatableCallback {
                    if (formTypeAndTitle.formType == FormType.EDIT || (formTypeAndTitle.formType == FormType.ADD && formTypeAndTitle.title != null)) {
                        setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                        saveDraftNavigationUseCase.invoke()
                        resetFormParamsUseCase.invoke()
                    } else {
                        hasSaveButtonBeingClicked.tryEmit(true)
                    }
                },
                isSubmitTitleButtonVisible = hasSaveButtonClicked == true && formTypeAndTitle.title.isNullOrEmpty(),
                submitTitleEvent = EquatableCallbackWithParam { title ->
                    if (title.isBlank()) setFormTitleUseCase.invoke(
                        formTypeAndTitle.formType,
                        null
                    ) else setFormTitleUseCase.invoke(formTypeAndTitle.formType, title)
                    resetFormParamsUseCase.invoke()
                    setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                },
                discardEvent = EquatableCallback {
                    clearPropertyFormNavigationUseCase.invoke()
                    setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                },
                isTitleTextInputVisible = hasSaveButtonClicked == true && formTypeAndTitle.title.isNullOrEmpty(),
            )
        }.collectLatest { emit(it) }
    }
}


