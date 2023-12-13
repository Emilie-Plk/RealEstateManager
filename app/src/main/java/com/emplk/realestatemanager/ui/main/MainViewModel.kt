package com.emplk.realestatemanager.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.current_property.GetCurrentPropertyIdFlowUseCase
import com.emplk.realestatemanager.domain.current_property.ResetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.filter.GetPropertiesFilterFlowUseCase
import com.emplk.realestatemanager.domain.filter.ResetPropertiesFilterUseCase
import com.emplk.realestatemanager.domain.navigation.GetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.navigation.GetToolbarSubtitleUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.ADD_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.DETAIL_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.DRAFTS_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.EDIT_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.FILTER_DIALOG_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.LIST_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.LOAN_SIMULATOR_DIALOG_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.MAP_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.SAVE_DRAFT_DIALOG_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.GetPropertiesCountUseCase
import com.emplk.realestatemanager.domain.property_draft.GetDraftsCountUseCase
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
    private val getToolbarSubtitleUseCase: GetToolbarSubtitleUseCase,
    private val resetCurrentPropertyIdUseCase: ResetCurrentPropertyIdUseCase,
    private val getDraftsCountUseCase: GetDraftsCountUseCase,
    private val resetPropertiesFilterUseCase: ResetPropertiesFilterUseCase,
    private val getPropertiesFilterFlowUseCase: GetPropertiesFilterFlowUseCase,
    private val getCurrentPropertyIdFlowUseCase: GetCurrentPropertyIdFlowUseCase,
    private val getPropertiesCountUseCase: GetPropertiesCountUseCase,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    private val isTabletMutableStateFlow = MutableStateFlow(false)

    val mainViewState: LiveData<MainViewState> = liveData(coroutineDispatcherProvider.main) {
        combine(
            isTabletMutableStateFlow.asStateFlow(),
            getNavigationTypeUseCase.invoke(),
            getToolbarSubtitleUseCase.invoke(),
            getPropertiesFilterFlowUseCase.invoke(),
            getPropertiesCountUseCase.invoke(),
        ) { isTablet, navigationType, toolbarSubtitle, filter, propertiesCount ->
            when (navigationType) {
                LIST_FRAGMENT -> if (!isTablet) {
                    emit(
                        MainViewState(
                            isAddFabVisible = true,
                            isFilterAppBarButtonVisible = filter == null && propertiesCount > 1,
                            isResetFilterAppBarButtonVisible = filter != null,
                            isAddAppBarButtonVisible = true,
                            subtitle = toolbarSubtitle
                        )
                    )
                } else {
                    emit(
                        MainViewState(
                            isAddFabVisible = false,
                            isFilterAppBarButtonVisible = filter == null && propertiesCount > 1,
                            isResetFilterAppBarButtonVisible = filter != null,
                            isAddAppBarButtonVisible = true,
                            subtitle = null
                        )
                    )
                }

                FILTER_DIALOG_FRAGMENT -> if (!isTablet) {
                    emit(
                        MainViewState(
                            false,
                            isFilterAppBarButtonVisible = filter == null && propertiesCount > 1,
                            isResetFilterAppBarButtonVisible = filter != null,
                            isAddAppBarButtonVisible = false,
                            subtitle = toolbarSubtitle
                        )
                    )
                } else {
                    emit(
                        MainViewState(
                            isAddFabVisible = false,
                            isFilterAppBarButtonVisible = filter == null && propertiesCount > 1,
                            isResetFilterAppBarButtonVisible = filter != null,
                            isAddAppBarButtonVisible = true,
                            subtitle = null
                        )
                    )
                }

                ADD_FRAGMENT -> resetCurrentPropertyIdUseCase.invoke()

                DETAIL_FRAGMENT -> if (!isTablet) {
                    emit(
                        MainViewState(
                            isAddFabVisible = false,
                            isFilterAppBarButtonVisible = false,
                            isResetFilterAppBarButtonVisible = false,
                            isAddAppBarButtonVisible = false,
                            subtitle = toolbarSubtitle
                        )
                    )
                } else {
                    emit(
                        MainViewState(
                            isAddFabVisible = false,
                            isFilterAppBarButtonVisible = false,
                            isResetFilterAppBarButtonVisible = false,
                            isAddAppBarButtonVisible = true,
                            subtitle = null
                        )
                    )
                }

                EDIT_FRAGMENT,
                LOAN_SIMULATOR_DIALOG_FRAGMENT,
                SAVE_DRAFT_DIALOG_FRAGMENT,
                MAP_FRAGMENT,
                DRAFTS_FRAGMENT -> Unit

            }
        }.collect()
    }

    val viewEventLiveData: LiveData<Event<MainViewEvent>> = liveData {
        combine(
            isTabletMutableStateFlow,
            getNavigationTypeUseCase.invoke(),
            getCurrentPropertyIdFlowUseCase.invoke(),
        ) { isTablet, navigationType, propertyId ->
            when (navigationType) {
                LIST_FRAGMENT -> Event(MainViewEvent.PropertyList)

                ADD_FRAGMENT -> {
                    when (getDraftsCountUseCase.invoke()) {
                        0 -> Event(MainViewEvent.NavigateToBlank(ADD_FRAGMENT.name))
                        else -> {
                            setNavigationTypeUseCase.invoke(DRAFTS_FRAGMENT)
                            Event(MainViewEvent.NoEvent)
                        }
                    }
                }

                EDIT_FRAGMENT -> Event(MainViewEvent.NavigateToBlank(EDIT_FRAGMENT.name))

                DETAIL_FRAGMENT ->
                    if (propertyId != null) {
                        if (!isTablet) {
                            Event(MainViewEvent.DetailOnPhone)
                        } else {
                            Event(MainViewEvent.DetailOnTablet)
                        }
                    } else {
                        Event(MainViewEvent.NavigateToBlank(LIST_FRAGMENT.name))
                    }


                FILTER_DIALOG_FRAGMENT -> Event(MainViewEvent.FilterProperties)

                DRAFTS_FRAGMENT -> Event(MainViewEvent.NavigateToBlank(DRAFTS_FRAGMENT.name))
                MAP_FRAGMENT -> Event(MainViewEvent.NavigateToBlank(MAP_FRAGMENT.name))
                LOAN_SIMULATOR_DIALOG_FRAGMENT -> Event(MainViewEvent.LoanSimulator)
                SAVE_DRAFT_DIALOG_FRAGMENT -> Event(MainViewEvent.NoEvent)
            }
        }.collectLatest { emit(it) }
    }

    fun onAddPropertyClicked() {
        setNavigationTypeUseCase.invoke(ADD_FRAGMENT)
    }

    fun onFilterPropertiesClicked() {
        setNavigationTypeUseCase.invoke(FILTER_DIALOG_FRAGMENT)
    }

    fun onResetFiltersClicked() {
        resetPropertiesFilterUseCase.invoke()
    }

    fun onMapClicked() {
        setNavigationTypeUseCase.invoke(MAP_FRAGMENT)
    }

    fun onLoanSimulatorClicked() {
        setNavigationTypeUseCase.invoke(LOAN_SIMULATOR_DIALOG_FRAGMENT)
    }

    fun onResume(isTablet: Boolean) {
        isTabletMutableStateFlow.value = isTablet
        setScreenWidthTypeUseCase.invoke(isTablet)
    }

    fun onBackClicked() {
        setNavigationTypeUseCase.invoke(LIST_FRAGMENT)
    }
}


