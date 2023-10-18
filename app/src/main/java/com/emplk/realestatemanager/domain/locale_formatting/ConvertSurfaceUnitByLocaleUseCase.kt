package com.emplk.realestatemanager.domain.locale_formatting

import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.Locale
import javax.inject.Inject
import kotlin.math.round


class ConvertSurfaceUnitByLocaleUseCase @Inject constructor(
    private val localeFormattingRepository: LocaleFormattingRepository,
) {
    fun invoke(surface: BigDecimal): BigDecimal =
        when (localeFormattingRepository.getLocale()) {
            Locale.US -> surface

            Locale.FRANCE -> localeFormattingRepository.convertSquareMetersToSquareFeet(surface)

            else -> throw IllegalStateException("Locale not supported")
        }
}