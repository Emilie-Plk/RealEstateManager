package com.emplk.realestatemanager.domain.screen_width

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject


class GetScreenWidthTypeFlowUseCase @Inject constructor(
    private val screenWidthTypeRepository: ScreenWidthTypeRepository
) {
    fun invoke(): Flow<ScreenWidthType> {
        Log.d("COUCOU SCREEN", "we enter the GetScreenWidthTypeFlowUseCase.invoke()")
        return screenWidthTypeRepository.getScreenWidthTypeAsFlow()
    }
}
