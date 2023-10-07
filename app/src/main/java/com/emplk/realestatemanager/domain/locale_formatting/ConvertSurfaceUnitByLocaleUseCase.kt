package com.emplk.realestatemanager.domain.locale_formatting

import javax.inject.Inject

class ConvertSurfaceUnitByLocaleUseCase @Inject constructor(
    private val localeFormattingRepository: LocaleFormattingRepository,
) {
    fun invoke(surface: Double): Double =
        when (localeFormattingRepository.getLocaleSurfaceUnitFormatting()) {
            SurfaceUnitType.SQUARE_FOOT -> surface

            SurfaceUnitType.SQUARE_METER -> localeFormattingRepository.convertSquareFeetToSquareMeters(surface)
        }
}