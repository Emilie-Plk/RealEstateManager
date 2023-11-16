package com.emplk.realestatemanager.domain.locale_formatting.surface

import com.emplk.realestatemanager.domain.locale_formatting.LocaleFormattingRepository
import javax.inject.Inject

class GetSurfaceUnitUseCase @Inject constructor(
    private val localeFormattingRepository: LocaleFormattingRepository,
) {
    fun invoke(): SurfaceUnitType = localeFormattingRepository.getLocaleSurfaceUnitFormatting()
}