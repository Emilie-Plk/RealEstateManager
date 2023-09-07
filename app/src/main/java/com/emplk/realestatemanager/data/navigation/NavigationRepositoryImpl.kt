package com.emplk.realestatemanager.data.navigation

import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.NavigationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class NavigationRepositoryImpl @Inject constructor() : NavigationRepository {
    private val navigationFragmentTypeMutableStateFlow = MutableStateFlow(NavigationFragmentType.LIST_FRAGMENT)
    // TODO: Event not State?

    override fun getNavigationFragmentType(): Flow<NavigationFragmentType> =
        navigationFragmentTypeMutableStateFlow.asStateFlow()

    override fun setNavigationFragmentType(navigationFragmentType: NavigationFragmentType) {
        navigationFragmentTypeMutableStateFlow.value = navigationFragmentType
    }
}