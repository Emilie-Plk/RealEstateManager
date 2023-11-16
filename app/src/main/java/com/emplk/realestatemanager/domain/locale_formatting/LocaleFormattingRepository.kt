package com.emplk.realestatemanager.domain.locale_formatting

import com.emplk.realestatemanager.domain.locale_formatting.currency.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.surface.SurfaceUnitType
import java.math.BigDecimal
import java.util.Locale

interface LocaleFormattingRepository {
    fun getLocale(): Locale
    fun convertSquareFeetToSquareMeters(squareFeet: BigDecimal): BigDecimal
    fun convertSquareMetersToSquareFeet(squareMeters: BigDecimal): BigDecimal
    fun getLocaleSurfaceUnitFormatting(): SurfaceUnitType
    fun convertDollarToEuro(dollar: BigDecimal, currencyRate: BigDecimal): BigDecimal
    fun convertEuroToDollar(euro: BigDecimal, currencyRate: BigDecimal): BigDecimal
    fun formatPriceToHumanReadable(price: BigDecimal): String
    fun getLocaleCurrencyFormatting(): CurrencyType
}