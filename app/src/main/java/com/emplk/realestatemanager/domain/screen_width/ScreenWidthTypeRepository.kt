package com.emplk.realestatemanager.domain.screen_width

import kotlinx.coroutines.flow.Flow

interface ScreenWidthTypeRepository {
    fun setScreenWidthType(isTablet: Boolean)
    fun getScreenWidthTypeAsFlow(): Flow<ScreenWidthType?>
}
