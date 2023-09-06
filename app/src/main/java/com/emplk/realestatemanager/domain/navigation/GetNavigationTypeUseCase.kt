package com.emplk.realestatemanager.domain.navigation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class GetNavigationTypeUseCase @Inject constructor(
    private val navigationRepository: NavigationRepository,
) {
    fun invoke(): Flow<NavigationFragmentType> = navigationRepository.getNavigationFragmentType()
}