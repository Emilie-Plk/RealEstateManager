package com.emplk.realestatemanager.domain.navigation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetNavigationTypeUseCase @Inject constructor(
    private val navigationRepository: NavigationRepository,
) {
    fun invoke(): Flow<NavigationFragmentType> = navigationRepository.getNavigationFragmentType()
}