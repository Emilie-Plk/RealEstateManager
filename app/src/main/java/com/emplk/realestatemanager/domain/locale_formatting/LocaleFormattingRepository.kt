package com.emplk.realestatemanager.domain.locale_formatting

import java.math.BigDecimal
import java.util.Locale

interface LocaleFormattingRepository {
    fun convertSquareFeetToSquareMeters(squareFeet: Double): Double
    fun convertSquareMetersToSquareFeet(squareMeters: Double): Double
    fun getLocale(): Locale
    fun getLocaleSurfaceUnitFormatting(): SurfaceUnitType
    fun convertDollarToEuro(dollar: BigDecimal, currencyRate: Double): BigDecimal
    fun convertEuroToDollar(euro: BigDecimal, currencyRate: Double): BigDecimal
    fun formatPrice(price: BigDecimal): String
    fun getLocaleCurrencyFormatting(): CurrencyType
}