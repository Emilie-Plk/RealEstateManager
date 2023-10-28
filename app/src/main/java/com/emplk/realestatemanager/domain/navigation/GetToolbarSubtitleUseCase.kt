package com.emplk.realestatemanager.domain.navigation

import com.emplk.realestatemanager.domain.screen_width.GetScreenWidthTypeFlowUseCase
import com.emplk.realestatemanager.domain.screen_width.ScreenWidthType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetToolbarSubtitleUseCase @Inject constructor(
    private val getScreenWidthTypeFlowUseCase: GetScreenWidthTypeFlowUseCase,
    private val getNavigationFragmentTypeFlowUseCase: GetNavigationTypeUseCase,
) {
    fun invoke(): Flow<String?> =
        combine(
            getNavigationFragmentTypeFlowUseCase.invoke(),
            getScreenWidthTypeFlowUseCase.invoke(),
        ) { navigationType, screenWidthType ->
            if (screenWidthType != ScreenWidthType.PHONE) {
                null
            } else {
                when (navigationType) {
                    NavigationFragmentType.EDIT_FRAGMENT -> "Edit property"
                    NavigationFragmentType.LIST_FRAGMENT -> "All properties"
                    NavigationFragmentType.FILTER_FRAGMENT -> "Filter properties"
                    NavigationFragmentType.ADD_FRAGMENT -> "Add property"
                    NavigationFragmentType.DETAIL_FRAGMENT -> "Property details"
                    NavigationFragmentType.DRAFT_DIALOG_FRAGMENT -> null
                    NavigationFragmentType.MAP_FRAGMENT -> "Map"
                    NavigationFragmentType.DRAFTS_FRAGMENT -> null
                    NavigationFragmentType.LOAN_SIMULATOR_DIALOG_FRAGMENT -> null
                }
            }
        }
}
