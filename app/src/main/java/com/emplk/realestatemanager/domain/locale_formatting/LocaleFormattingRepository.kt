package com.emplk.realestatemanager.domain.locale_formatting

import java.math.BigDecimal
import java.util.Locale

interface LocaleFormattingRepository {
    fun convertSquareFeetToSquareMeters(squareFeet: Double): Double
    fun convertSquareMetersToSquareFeet(squareMeters: Double): Double
    fun convertDollarToEuro(dollar: BigDecimal): BigDecimal
    fun convertEuroToDollar(euro: BigDecimal): BigDecimal
    fun getLocale(): Locale
    fun formatPrice(price: BigDecimal): String
    fun getLocaleCurrencyFormatting(): CurrencyType
    fun getLocaleSurfaceUnitFormatting(): SurfaceUnitType
}