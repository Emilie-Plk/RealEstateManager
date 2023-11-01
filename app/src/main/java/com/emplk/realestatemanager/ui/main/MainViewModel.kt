package com.emplk.realestatemanager.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.current_property.GetCurrentPropertyIdFlowUseCase
import com.emplk.realestatemanager.domain.current_property.ResetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.navigation.GetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.navigation.GetToolbarSubtitleUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.ADD_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.DETAIL_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.DRAFTS_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.DRAFT_DIALOG_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.EDIT_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.FILTER_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.LIST_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.LOAN_SIMULATOR_DIALOG_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType.MAP_FRAGMENT
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property_draft.GetDraftsCountUseCase
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
    private val getToolbarSubtitleUseCase: GetToolbarSubtitleUseCase,
    private val resetCurrentPropertyIdUseCase: ResetCurrentPropertyIdUseCase,
    private val getDraftsCountUseCase: GetDraftsCountUseCase,
    private val getCurrentPropertyIdFlowUseCase: GetCurrentPropertyIdFlowUseCase,
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

                ADD_FRAGMENT -> resetCurrentPropertyIdUseCase.invoke()

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

                LOAN_SIMULATOR_DIALOG_FRAGMENT,
                DRAFT_DIALOG_FRAGMENT,
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
                LIST_FRAGMENT -> emit(Event(MainViewEvent.PropertyList))

                ADD_FRAGMENT -> {
                    when (getDraftsCountUseCase.invoke()) {
                        0 -> emit(Event(MainViewEvent.NavigateToBlank(ADD_FRAGMENT.name, propertyId)))
                        else -> setNavigationTypeUseCase.invoke(DRAFTS_FRAGMENT)
                    }

                }

                EDIT_FRAGMENT -> emit(Event(MainViewEvent.NavigateToBlank(EDIT_FRAGMENT.name, propertyId)))

                DETAIL_FRAGMENT ->
                    if (propertyId != null) {
                        if (!isTablet) {
                            emit(Event(MainViewEvent.DetailFragmentOnPhone(propertyId)))
                        } else {
                            emit(Event(MainViewEvent.DetailFragmentOnTablet(propertyId)))
                        }
                    }

                FILTER_FRAGMENT ->
                    if (!isTablet) {
                        emit(Event(MainViewEvent.FilterPropertiesFragmentOnPhone))
                    } else {
                        emit(Event(MainViewEvent.FilterPropertiesFragmentOnTablet))
                    }

                DRAFTS_FRAGMENT -> emit(Event(MainViewEvent.NavigateToBlank(DRAFTS_FRAGMENT.name, null)))
                MAP_FRAGMENT -> emit(Event(MainViewEvent.NavigateToBlank(MAP_FRAGMENT.name, null)))
                LOAN_SIMULATOR_DIALOG_FRAGMENT -> emit(Event(MainViewEvent.LoanSimulator))
                DRAFT_DIALOG_FRAGMENT -> Unit
            }
        }.collect()
    }

    fun onAddPropertyClicked() {
        setNavigationTypeUseCase.invoke(ADD_FRAGMENT)
    }

    fun onFilterPropertiesClicked() {
        setNavigationTypeUseCase.invoke(FILTER_FRAGMENT)
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

    fun onNavigationChanged(navigationFragmentTypeString: String) =
        setNavigationTypeUseCase.invoke(NavigationFragmentType.valueOf(navigationFragmentTypeString))
}