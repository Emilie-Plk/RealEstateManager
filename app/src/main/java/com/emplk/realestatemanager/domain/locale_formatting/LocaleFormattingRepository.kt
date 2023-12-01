package com.emplk.realestatemanager.domain.locale_formatting

import com.emplk.realestatemanager.domain.locale_formatting.currency.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.surface.SurfaceUnitType
import java.math.BigDecimal
import java.util.Locale

interface LocaleFormattingRepository {
    fun getLocale(): Locale
    fun convertSquareFeetToSquareMetersRoundedHalfUp(squareFeet: BigDecimal): BigDecimal
    fun convertSquareMetersToSquareFeetRoundedHalfUp(squareMeters: BigDecimal): BigDecimal
    fun getLocaleSurfaceUnitFormatting(): SurfaceUnitType
    fun convertDollarToEuroRoundedHalfUp(dollar: BigDecimal, currencyRate: BigDecimal): BigDecimal
    fun convertEuroToDollarRoundedHalfUp(euro: BigDecimal, currencyRate: BigDecimal): BigDecimal
    fun formatRoundedPriceToHumanReadable(price: BigDecimal): String
    fun getLocaleCurrencyFormatting(): CurrencyType
}