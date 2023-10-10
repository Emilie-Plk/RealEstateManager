package com.emplk.realestatemanager.domain.locale_formatting

import javax.inject.Inject

class GetRoundedSurfaceWithSurfaceUnitUseCase @Inject constructor(
    private val getSurfaceUnitUseCase: GetSurfaceUnitUseCase,
) {
    fun invoke(surface: Double): String {
        val roundedSurface = kotlin.math.ceil(surface).toInt()
        return when (val surfaceUnitType = getSurfaceUnitUseCase.invoke()) {
            SurfaceUnitType.SQUARE_FOOT -> "$roundedSurface ${surfaceUnitType.symbol}"
            SurfaceUnitType.SQUARE_METER -> "$roundedSurface ${surfaceUnitType.symbol}"
        }
    }
}