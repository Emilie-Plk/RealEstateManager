package com.emplk.realestatemanager.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.current_property.GetCurrentPropertyIdFlowUseCase
import com.emplk.realestatemanager.domain.navigation.GetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.navigation.GetToolbarSubtitleUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.*
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.screen_width.SetScreenWidthTypeUseCase
import com.emplk.realestatemanager.ui.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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

    val mainViewState: LiveData<MainViewState> = liveData(coroutineDispatcherProvider.main) {
        combine(
            isTabletMutableStateFlow.asStateFlow(),
            getNavigationTypeUseCase.invoke(),
            getToolbarSubtitleUseCase.invoke(),
        ) { isTablet, navigationType, toolbarSubtitle ->
            when (navigationType) {
                LIST_FRAGMENT -> if (!isTablet) {
                    emit(
                        MainViewState(
                            isAddFabVisible = true,
                            isFilterAppBarButtonVisible = true,
                            isAddAppBarButtonVisible = true,
                            subtitle = toolbarSubtitle
                        )
                    )
                } else {
                    emit(
                        MainViewState(
                            isAddFabVisible = false,
                            isFilterAppBarButtonVisible = true,
                            isAddAppBarButtonVisible = true,
                            subtitle = null
                        )
                    )
                }

                EDIT_FRAGMENT -> Unit

                FILTER_FRAGMENT -> if (!isTablet) {
                    emit(
                        MainViewState(
                            false,
                            isFilterAppBarButtonVisible = false,
                            isAddAppBarButtonVisible = false,
                            subtitle = toolbarSubtitle
                        )
                    )
                } else {
                    emit(
                        MainViewState(
                            isAddFabVisible = false,
                            isFilterAppBarButtonVisible = false,
                            isAddAppBarButtonVisible = true,
                            subtitle = null
                        )
                    )
                }

                ADD_FRAGMENT -> Unit

                DETAIL_FRAGMENT -> if (!isTablet) {
                    emit(
                        MainViewState(
                            false,
                            isFilterAppBarButtonVisible = false,
                            isAddAppBarButtonVisible = false,
                            subtitle = toolbarSubtitle
                        )
                    )
                } else {
                    emit(
                        MainViewState(
                            false,
                            isFilterAppBarButtonVisible = true,
                            isAddAppBarButtonVisible = true,
                            subtitle = null
                        )
                    )
                }

                DRAFT_DIALOG_FRAGMENT -> Unit
            }
        }.collect()
    }

    val viewEventLiveData: LiveData<Event<MainViewEvent>> = liveData {
        combine(
            isTabletMutableStateFlow.asStateFlow(),
            getNavigationTypeUseCase.invoke(),
            getCurrentPropertyIdFlowUseCase.invoke(),
        ) { isTablet, navigationType, currentPropertyId ->
            when (navigationType) {
                LIST_FRAGMENT -> emit(Event(MainViewEvent.DisplayPropertyList))

                ADD_FRAGMENT -> {
                    emit(Event(MainViewEvent.NavigateToBlank(ADD_FRAGMENT.name)))
                }

                EDIT_FRAGMENT -> {
                    emit(Event(MainViewEvent.NavigateToBlank(EDIT_FRAGMENT.name)))
                }

                DETAIL_FRAGMENT ->
                    if (currentPropertyId >= 1) {
                        if (!isTablet) {
                            emit(Event(MainViewEvent.DisplayDetailFragmentOnPhone))
                        } else {
                            emit(Event(MainViewEvent.DisplayDetailFragmentOnTablet))
                        }
                    }

                FILTER_FRAGMENT ->
                    if (!isTablet) {
                        emit(Event(MainViewEvent.DisplayFilterPropertiesFragmentOnPhone))
                    } else {
                        emit(Event(MainViewEvent.DisplayFilterPropertiesFragmentOnTablet))
                    }

                DRAFT_DIALOG_FRAGMENT -> Unit
            }
        }.collect()
    }

    fun onAddPropertyClicked() {
        setNavigationTypeUseCase.invoke(
            ADD_FRAGMENT
        )
    }

    fun onFilterPropertiesClicked() {
        setNavigationTypeUseCase.invoke(
            FILTER_FRAGMENT
        )
    }

    fun onResume(isTablet: Boolean) {
        isTabletMutableStateFlow.value = isTablet
        setScreenWidthTypeUseCase.invoke(isTablet)
    }
}