package com.emplk.realestatemanager.domain.navigation

import com.emplk.realestatemanager.domain.filter.GetPropertiesFilterFlowUseCase
import com.emplk.realestatemanager.domain.screen_width.GetScreenWidthTypeFlowUseCase
import com.emplk.realestatemanager.domain.screen_width.ScreenWidthType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetToolbarSubtitleUseCase @Inject constructor(
    private val getScreenWidthTypeFlowUseCase: GetScreenWidthTypeFlowUseCase,
    private val getNavigationFragmentTypeFlowUseCase: GetNavigationTypeUseCase,
    private val getPropertiesFilterFlowUseCase: GetPropertiesFilterFlowUseCase,
) {
    fun invoke(): Flow<String?> =
        combine(
            getNavigationFragmentTypeFlowUseCase.invoke(),
            getScreenWidthTypeFlowUseCase.invoke(),
            getPropertiesFilterFlowUseCase.invoke(),
        ) { navigationType, screenWidthType, propertiesFilter ->
            if (screenWidthType != ScreenWidthType.PHONE) {
                null
            } else {
                when (navigationType) {
                    NavigationFragmentType.EDIT_FRAGMENT -> "Edit property"
                    NavigationFragmentType.LIST_FRAGMENT -> if (propertiesFilter != null) {
                        "Filtered properties"
                    } else {
                        null
                    }

                    NavigationFragmentType.ADD_FRAGMENT -> "Add property"
                    NavigationFragmentType.DETAIL_FRAGMENT -> "Property details"
                    NavigationFragmentType.MAP_FRAGMENT -> "Map"
                    NavigationFragmentType.DRAFT_DIALOG_FRAGMENT,
                    NavigationFragmentType.DRAFTS_FRAGMENT,
                    NavigationFragmentType.FILTER_DIALOG_FRAGMENT,
                    NavigationFragmentType.LOAN_SIMULATOR_DIALOG_FRAGMENT -> null
                }
            }
        }
}
