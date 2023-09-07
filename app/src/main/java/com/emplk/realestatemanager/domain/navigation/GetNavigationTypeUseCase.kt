package com.emplk.realestatemanager.domain.navigation

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNavigationTypeUseCase @Inject constructor(
    private val navigationRepository: NavigationRepository,
) {
    fun invoke(): Flow<NavigationFragmentType> = navigationRepository.getNavigationFragmentType()
}