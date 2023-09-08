package com.emplk.realestatemanager.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.current_property.GetCurrentPropertyIdFlowUseCase
import com.emplk.realestatemanager.domain.navigation.GetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.navigation.GetToolbarSubtitleUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.ADD_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.DETAIL_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.EDIT_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.FILTER_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.LIST_FRAGMENT
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
    private val getToolbarSubtitleUseCase: GetToolbarSubtitleUseCase,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {
    private val isTabletMutableStateFlow = MutableStateFlow(false)

    val isFabVisibleLiveData: LiveData<Boolean> = liveData(coroutineDispatcherProvider.main) {
        combine(
            isTabletMutableStateFlow.asStateFlow(),
            getNavigationTypeUseCase.invoke(),
        ) { isTablet, navigationType ->
            when (navigationType) {
                LIST_FRAGMENT -> !isTablet
                else -> false
            }
        }.collectLatest { emit(it) }
    }

    val viewEventLiveData: LiveData<Event<MainViewEvent>> = liveData {
        combine(
            isTabletMutableStateFlow.asStateFlow(),
            getNavigationTypeUseCase.invoke(),
            getCurrentPropertyIdFlowUseCase.invoke(),
        ) { isTablet, navigationType, currentPropertyId ->
            when (navigationType) {
                LIST_FRAGMENT ->
                    if (!isTablet) {
                        emit(Event(MainViewEvent.DisplayPropertyListFragmentOnPhone))
                    } else {
                        emit(Event(MainViewEvent.DisplayPropertyListFragmentOnTablet))
                    }

                ADD_FRAGMENT ->
                    if (!isTablet) {
                        emit(Event(MainViewEvent.DisplayAddPropertyFragmentOnPhone))
                    } else {
                        emit(Event(MainViewEvent.DisplayAddPropertyFragmentOnTablet))
                    }

                DETAIL_FRAGMENT ->
                    if (currentPropertyId >= 1) {
                        if (!isTablet) {
                            emit(Event(MainViewEvent.DisplayDetailFragmentOnPhone))
                        } else {
                            emit(Event(MainViewEvent.DisplayDetailFragmentOnTablet))
                        }
                    }

                EDIT_FRAGMENT ->
                    if (currentPropertyId >= 1) {
                        if (!isTablet) {
                            emit(Event(MainViewEvent.DisplayEditPropertyFragmentOnPhone))
                        } else {
                            emit(Event(MainViewEvent.DisplayEditPropertyFragmentOnTablet))
                        }
                    }

                FILTER_FRAGMENT ->
                    if (!isTablet) {
                        emit(Event(MainViewEvent.DisplayFilterPropertiesFragmentOnPhone))
                    } else {
                        emit(Event(MainViewEvent.DisplayFilterPropertiesFragmentOnTablet))
                    }
            }
        }.collect()
    }

    val toolbarSubtitleLiveData: LiveData<String?> = getToolbarSubtitleUseCase.invoke().asLiveData()


    fun onAddPropertyClicked() {
        setNavigationTypeUseCase.invoke(
            ADD_FRAGMENT
        )
    }

    fun onResume(isTablet: Boolean) {
        isTabletMutableStateFlow.value = isTablet
       // setScreenWidthTypeUseCase.invoke(isTablet)
    }

    fun onFilterPropertiesClicked() {
        setNavigationTypeUseCase.invoke(
            FILTER_FRAGMENT
        )
    }
}