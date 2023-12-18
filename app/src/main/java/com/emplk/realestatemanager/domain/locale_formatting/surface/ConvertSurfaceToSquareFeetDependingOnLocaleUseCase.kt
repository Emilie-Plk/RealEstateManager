package com.emplk.realestatemanager.domain.locale_formatting.surface

import com.emplk.realestatemanager.domain.locale_formatting.HumanReadableRepository
import java.math.BigDecimal
import java.util.Locale
import javax.inject.Inject


class ConvertSurfaceToSquareFeetDependingOnLocaleUseCase @Inject constructor(
    private val humanReadableRepository: HumanReadableRepository,
) {
    /**
     * @return BigDecimal of the surface converted to the unit of measurement depending on locale
     * (it is used to save surface in sq ft in database)
     */
    fun invoke(surface: BigDecimal): BigDecimal =
        when (humanReadableRepository.getLocale()) {
            Locale.US -> surface
            Locale.FRANCE -> humanReadableRepository.convertSquareMetersToSquareFeetRoundedHalfUp(surface)
            else -> surface
        }
}