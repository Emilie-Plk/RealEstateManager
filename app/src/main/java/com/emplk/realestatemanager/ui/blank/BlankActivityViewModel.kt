package com.emplk.realestatemanager.ui.blank

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.domain.current_property.GetCurrentPropertyIdFlowUseCase
import com.emplk.realestatemanager.domain.navigation.GetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property_draft.IsPropertyFormInProgressUseCase
import com.emplk.realestatemanager.ui.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlankActivityViewModel @Inject constructor(
    private val getNavigationTypeUseCase: GetNavigationTypeUseCase,
    private val isPropertyFormInProgressUseCase: IsPropertyFormInProgressUseCase,
    private val getCurrentPropertyIdFlowUseCase: GetCurrentPropertyIdFlowUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
) : ViewModel() {

    val viewEventLiveData: LiveData<Event<BlankViewEvent>> = liveData {
        getNavigationTypeUseCase.invoke().collectLatest { navigationType ->
            when (navigationType) {
                NavigationFragmentType.ADD_FRAGMENT -> emit(Event(BlankViewEvent.OnAddNewDraftClicked))
                NavigationFragmentType.EDIT_FRAGMENT -> {
                    getCurrentPropertyIdFlowUseCase.invoke().filterNotNull().collectLatest { id ->
                        emit(Event(BlankViewEvent.OnDraftClicked(id)))
                    }
                }

                NavigationFragmentType.LIST_FRAGMENT -> emit(Event(BlankViewEvent.NavigateToMain(NavigationFragmentType.LIST_FRAGMENT.name)))
                NavigationFragmentType.SAVE_DRAFT_DIALOG_FRAGMENT -> emit(Event(BlankViewEvent.SaveDraftDialog))
                NavigationFragmentType.FILTER_DIALOG_FRAGMENT,
                NavigationFragmentType.DETAIL_FRAGMENT,
                NavigationFragmentType.MAP_FRAGMENT,
                NavigationFragmentType.LOAN_SIMULATOR_DIALOG_FRAGMENT,
                NavigationFragmentType.DRAFTS_FRAGMENT -> Unit
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            isPropertyFormInProgressUseCase.invoke().collect { isPropertyFormInProgress ->
                if (isPropertyFormInProgress) {
                    setNavigationTypeUseCase.invoke(NavigationFragmentType.SAVE_DRAFT_DIALOG_FRAGMENT)
                } else {
                    setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                }
            }
        }

    }
}
