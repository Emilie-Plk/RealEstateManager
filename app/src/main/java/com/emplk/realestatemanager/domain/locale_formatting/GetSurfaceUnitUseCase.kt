package com.emplk.realestatemanager.domain.locale_formatting

import javax.inject.Inject

class GetSurfaceUnitUseCase @Inject constructor(
    private val localeFormattingRepository: LocaleFormattingRepository,
) {
    fun invoke(): SurfaceUnitType = localeFormattingRepository.getLocaleSurfaceUnitFormatting()
}