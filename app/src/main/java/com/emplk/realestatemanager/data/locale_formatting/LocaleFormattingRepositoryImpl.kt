package com.emplk.realestatemanager.data.locale_formatting

import android.content.res.Resources
import com.emplk.realestatemanager.domain.locale_formatting.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.LocaleFormattingRepository
import com.emplk.realestatemanager.domain.locale_formatting.SurfaceUnitType
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

class LocaleFormattingRepositoryImpl @Inject constructor(
    private val resources: Resources,
    private val locale: Locale,
) : LocaleFormattingRepository {

    companion object {
        private const val SQUARE_FEET_TO_SQUARE_METERS = 10.764
        private val EURO_TO_USD_EXCHANGE_RATE = BigDecimal(1.07) // as of 10/07/2021
    }

    override fun convertSquareFeetToSquareMeters(squareFeet: Double): Double =
        squareFeet / SQUARE_FEET_TO_SQUARE_METERS

    override fun convertSquareMetersToSquareFeet(squareMeters: Double): Double =
        squareMeters * SQUARE_FEET_TO_SQUARE_METERS

    override fun convertDollarToEuro(dollar: BigDecimal): BigDecimal =
        dollar.multiply(EURO_TO_USD_EXCHANGE_RATE)


    override fun convertEuroToDollar(euro: BigDecimal): BigDecimal =
        euro.divide(EURO_TO_USD_EXCHANGE_RATE, 2, RoundingMode.HALF_UP)

    override fun getLocale(): Locale = locale

    override fun formatPrice(price: BigDecimal): String {
        val roundedPrice = price.setScale(2, RoundingMode.HALF_UP)

        val numberFormat = NumberFormat.getCurrencyInstance(locale)
        numberFormat.maximumFractionDigits = 0
        return numberFormat.format(roundedPrice)
    }

    override fun getLocaleCurrencyFormatting(): CurrencyType = when (locale) {
        Locale.FRANCE -> CurrencyType.EURO
        Locale.US -> CurrencyType.DOLLAR
        else -> CurrencyType.DOLLAR
    }

    override fun getLocaleSurfaceUnitFormatting(): SurfaceUnitType = when (locale) {
        Locale.FRANCE -> SurfaceUnitType.SQUARE_METER
        Locale.US -> SurfaceUnitType.SQUARE_FOOT
        else -> SurfaceUnitType.SQUARE_FOOT
    }
}