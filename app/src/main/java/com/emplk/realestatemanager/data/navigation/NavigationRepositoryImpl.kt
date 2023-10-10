package com.emplk.realestatemanager.data.navigation

import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.NavigationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class NavigationRepositoryImpl @Inject constructor() : NavigationRepository {
    private val navigationFragmentTypeMutableSharedFlow =
        MutableSharedFlow<NavigationFragmentType>(extraBufferCapacity = 1)

    override fun getNavigationFragmentType(): Flow<NavigationFragmentType> =
        navigationFragmentTypeMutableSharedFlow

    override fun setNavigationFragmentType(navigationFragmentType: NavigationFragmentType) {
        navigationFragmentTypeMutableSharedFlow.tryEmit(navigationFragmentType)
    }
}