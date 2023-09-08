package com.emplk.realestatemanager.data.navigation

import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.NavigationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class NavigationRepositoryImpl @Inject constructor() : NavigationRepository {
    private val navigationFragmentTypeMutableSharedFlow =
        MutableSharedFlow<NavigationFragmentType>(replay = 1).apply {
            tryEmit(NavigationFragmentType.LIST_FRAGMENT)
        }  // TODO: NINO I think I should eventify it...

    override fun getNavigationFragmentType(): Flow<NavigationFragmentType> =
        navigationFragmentTypeMutableSharedFlow.asSharedFlow()

    override fun setNavigationFragmentType(navigationFragmentType: NavigationFragmentType) {
        navigationFragmentTypeMutableSharedFlow.tryEmit(navigationFragmentType)
    }
}