package com.emplk.realestatemanager.ui.blank

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.domain.navigation.GetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property_draft.IsPropertyFormInProgressUseCase
import com.emplk.realestatemanager.ui.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlankActivityViewModel @Inject constructor(
    private val getNavigationTypeUseCase: GetNavigationTypeUseCase,
    private val isPropertyFormInProgressUseCase: IsPropertyFormInProgressUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
) : ViewModel() {

    val viewEventLiveData: LiveData<Event<BlankViewEvent>> = liveData {
        getNavigationTypeUseCase.invoke().collect { navigationType ->
            when (navigationType) {
                NavigationFragmentType.ADD_FRAGMENT -> Unit
                NavigationFragmentType.EDIT_FRAGMENT -> Unit
                NavigationFragmentType.FILTER_FRAGMENT -> Unit
                NavigationFragmentType.DETAIL_FRAGMENT -> Unit
                NavigationFragmentType.LIST_FRAGMENT -> emit(Event(BlankViewEvent.NavigateToMain))
                NavigationFragmentType.DRAFT_DIALOG_FRAGMENT -> emit(Event(BlankViewEvent.DisplayDraftDialog))
                NavigationFragmentType.MAP_FRAGMENT -> Unit
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            isPropertyFormInProgressUseCase.invoke().collect { isPropertyFormInProgress ->
                if (isPropertyFormInProgress) {
                    setNavigationTypeUseCase.invoke(NavigationFragmentType.DRAFT_DIALOG_FRAGMENT)
                } else {
                    setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                }
            }
        }

    }
}
