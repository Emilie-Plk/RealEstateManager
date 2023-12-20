package com.emplk.realestatemanager.domain.locale_formatting.surface

import android.util.Log
import com.emplk.realestatemanager.domain.locale_formatting.GetLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.HumanReadableRepository
import java.math.BigDecimal
import java.util.Locale
import javax.inject.Inject

class ConvertToSquareFeetDependingOnLocaleUseCase @Inject constructor(
    private val humanReadableRepository: HumanReadableRepository,
    private val getLocaleUseCase: GetLocaleUseCase,
) {
    /**
     * @return BigDecimal of the surface converted to the unit of measurement depending on locale
     */
    fun invoke(surface: BigDecimal): BigDecimal =
        when (getLocaleUseCase.invoke()) {
            Locale.US -> surface
            Locale.FRANCE -> humanReadableRepository.convertSquareFeetToSquareMetersRoundedHalfUp(surface)
            else -> surface
        }.also { Log.d("COUCOU", "invoke of ConvertToSquareFeetDependingOnLocaleUseCase : $surface") }
}