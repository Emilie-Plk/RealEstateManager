package com.emplk.realestatemanager.domain.locale_formatting

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Locale

interface LocaleFormattingRepository {
    fun convertSquareFeetToSquareMeters(squareFeet: BigDecimal): BigDecimal
    fun convertSquareMetersToSquareFeet(squareMeters: BigDecimal): BigDecimal
    fun getLocale(): Locale
    fun getLocaleSurfaceUnitFormatting(): SurfaceUnitType
    fun convertDollarToEuro(dollar: BigDecimal, currencyRate: BigDecimal): BigDecimal
    fun convertEuroToDollar(euro: BigDecimal, currencyRate: BigDecimal): BigDecimal
    fun formatPrice(price: BigDecimal): String
    fun getLocaleCurrencyFormatting(): CurrencyType
}