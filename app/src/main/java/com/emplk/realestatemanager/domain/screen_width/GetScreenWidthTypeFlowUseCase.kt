package com.emplk.realestatemanager.domain.screen_width

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject


class GetScreenWidthTypeFlowUseCase @Inject constructor(
    private val screenWidthTypeRepository: ScreenWidthTypeRepository
) {
    fun invoke(): Flow<ScreenWidthType> =
        screenWidthTypeRepository.getScreenWidthTypeAsFlow()
}
