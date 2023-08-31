package com.emplk.realestatemanager.domain.navigation

import javax.inject.Inject

class SetNavigationTypeUseCase @Inject constructor(
    private val navigationRepository: NavigationRepository,
) {
    fun invoke(navigationFragmentType: NavigationFragmentType) {
        navigationRepository.setNavigationFragmentType(navigationFragmentType)
    }
}