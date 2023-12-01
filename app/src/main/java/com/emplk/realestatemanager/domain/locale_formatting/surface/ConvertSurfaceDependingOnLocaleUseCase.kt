package com.emplk.realestatemanager.domain.locale_formatting.surface

import com.emplk.realestatemanager.domain.locale_formatting.GetLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.LocaleFormattingRepository
import java.math.BigDecimal
import java.util.Locale
import javax.inject.Inject


class ConvertSurfaceDependingOnLocaleUseCase @Inject constructor(
    private val localeFormattingRepository: LocaleFormattingRepository,
    private val getLocaleUseCase: GetLocaleUseCase,
) {
    /**
     * @return BigDecimal of the surface converted to the unit of measurement depending on locale
     */
    fun invoke(surface: BigDecimal): BigDecimal =
        when (getLocaleUseCase.invoke()) {
            Locale.US -> surface
            Locale.FRANCE -> localeFormattingRepository.convertSquareFeetToSquareMetersRoundedHalfUp(surface)
            else -> surface
        }
}