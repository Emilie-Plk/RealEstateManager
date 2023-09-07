package com.emplk.realestatemanager.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.current_property.GetCurrentPropertyIdFlowUseCase
import com.emplk.realestatemanager.domain.navigation.GetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.screen_width.SetScreenWidthTypeUseCase
import com.emplk.realestatemanager.ui.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val setScreenWidthTypeUseCase: SetScreenWidthTypeUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val getNavigationTypeUseCase: GetNavigationTypeUseCase,
    private val getCurrentPropertyIdFlowUseCase: GetCurrentPropertyIdFlowUseCase,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {
    private val isTabletMutableStateFlow = MutableStateFlow(false)

    val isFabVisibleLiveData: LiveData<Boolean> = liveData(coroutineDispatcherProvider.io) {
        combine(
            isTabletMutableStateFlow.asStateFlow(),
            getNavigationTypeUseCase.invoke(),
        ) { isTablet, navigationType ->
            when (navigationType) {
                NavigationFragmentType.LIST_FRAGMENT -> !isTablet
                else -> false
            }
        }.collectLatest { emit(it) }
    }

    val viewEventLiveData: LiveData<Event<MainViewEvent>> = liveData(coroutineDispatcherProvider.io) {
        combine(
            isTabletMutableStateFlow.asStateFlow(),
            getNavigationTypeUseCase.invoke(),
            getCurrentPropertyIdFlowUseCase.invoke(),
        ) { isTablet, navigationType, currentPropertyId ->
            when (navigationType) {
                NavigationFragmentType.LIST_FRAGMENT ->
                    if (!isTablet) {
                        emit(Event(MainViewEvent.DisplayPropertyListFragmentOnPhone))
                    } else {
                        emit(Event(MainViewEvent.DisplayPropertyListFragmentOnTablet))
                    }

                NavigationFragmentType.ADD_FRAGMENT ->
                    if (!isTablet) {
                        emit(Event(MainViewEvent.DisplayAddPropertyFragmentOnPhone))
                    } else {
                        emit(Event(MainViewEvent.DisplayAddPropertyFragmentOnTablet))
                    }

                NavigationFragmentType.DETAIL_FRAGMENT ->
                    if (currentPropertyId >= 1) {
                        if (!isTablet) {
                            emit(Event(MainViewEvent.StartDetailActivity))
                        } else {
                            emit(Event(MainViewEvent.DisplayDetailFragment))
                        }
                    }

                NavigationFragmentType.EDIT_FRAGMENT ->
                    if (currentPropertyId >= 1) {
                        if (!isTablet) {
                            emit(Event(MainViewEvent.DisplayEditPropertyFragmentOnPhone))
                        } else {
                            emit(Event(MainViewEvent.DisplayEditPropertyFragmentOnTablet))
                        }
                    }
            }
        }.collect()
    }

    fun onAddPropertyClicked() {
        setNavigationTypeUseCase.invoke(
            NavigationFragmentType.ADD_FRAGMENT
        )
    }

    fun onResume(isTablet: Boolean) {
        isTabletMutableStateFlow.value = isTablet
        setScreenWidthTypeUseCase.invoke(isTablet)
    }
}