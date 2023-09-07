package com.emplk.realestatemanager.data.screen_width

import com.emplk.realestatemanager.domain.screen_width.ScreenWidthType
import com.emplk.realestatemanager.domain.screen_width.ScreenWidthTypeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class ScreenWidthTypeRepositoryImpl @Inject constructor() : ScreenWidthTypeRepository {
    private val screenWidthMutableStateFlow = MutableStateFlow(ScreenWidthType.UNDEFINED)

    override fun setScreenWidthType(isTablet: Boolean) {
        when (isTablet) {
            true -> screenWidthMutableStateFlow.value = ScreenWidthType.TABLET
            false -> screenWidthMutableStateFlow.value = ScreenWidthType.PHONE
        }
    }

    override fun getScreenWidthTypeAsFlow() =
        screenWidthMutableStateFlow.asSharedFlow()
}