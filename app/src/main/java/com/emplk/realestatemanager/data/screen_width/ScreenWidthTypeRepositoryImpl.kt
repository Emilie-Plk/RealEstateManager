package com.emplk.realestatemanager.data.screen_width

import com.emplk.realestatemanager.domain.screen_width.ScreenWidthType
import com.emplk.realestatemanager.domain.screen_width.ScreenWidthTypeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class ScreenWidthTypeRepositoryImpl @Inject constructor() : ScreenWidthTypeRepository {
    private val screenWidthMutableStateFlow: MutableStateFlow<ScreenWidthType?> = MutableStateFlow(null)

    override fun setScreenWidthType(isTablet: Boolean) {
        when (isTablet) {
            true -> screenWidthMutableStateFlow.tryEmit(ScreenWidthType.TABLET)
            false -> screenWidthMutableStateFlow.tryEmit(ScreenWidthType.PHONE)
        }
    }

    override fun getScreenWidthTypeAsFlow(): Flow<ScreenWidthType?> =
        screenWidthMutableStateFlow.asSharedFlow()
}