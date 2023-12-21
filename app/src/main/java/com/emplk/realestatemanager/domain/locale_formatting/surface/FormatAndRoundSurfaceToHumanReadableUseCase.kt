package com.emplk.realestatemanager.domain.locale_formatting.surface

import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class FormatAndRoundSurfaceToHumanReadableUseCase @Inject constructor(
    private val getSurfaceUnitUseCase: GetSurfaceUnitUseCase,
) {
    fun invoke(surface: BigDecimal): String {
        val roundedSurface = surface.setScale(0, RoundingMode.HALF_UP)
        return when (val surfaceUnitType = getSurfaceUnitUseCase.invoke()) {
            SurfaceUnitType.SQUARE_FOOT -> "$roundedSurface ${surfaceUnitType.symbol}"
            SurfaceUnitType.SQUARE_METER -> "$roundedSurface ${surfaceUnitType.symbol}"
        }
    }
}