package com.emplk.realestatemanager.domain.screen_width

import javax.inject.Inject

class SetScreenWidthTypeUseCase @Inject constructor(
    private val screenWidthTypeRepository: ScreenWidthTypeRepository
) {
    fun invoke(isTablet: Boolean) {
        screenWidthTypeRepository.setScreenWidthType(isTablet)
    }
}