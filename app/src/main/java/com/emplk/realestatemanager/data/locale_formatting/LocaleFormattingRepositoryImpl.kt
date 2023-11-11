package com.emplk.realestatemanager.data.locale_formatting

import com.emplk.realestatemanager.domain.locale_formatting.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.LocaleFormattingRepository
import com.emplk.realestatemanager.domain.locale_formatting.SurfaceUnitType
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

class LocaleFormattingRepositoryImpl @Inject constructor(
    private val locale: Locale,
) : LocaleFormattingRepository {

    companion object {
        private const val SQUARE_FEET_TO_SQUARE_METERS = 10.764
    }

    override fun getLocale(): Locale = locale

    override fun convertSquareFeetToSquareMeters(squareFeet: BigDecimal): BigDecimal =
        squareFeet.divide(BigDecimal(SQUARE_FEET_TO_SQUARE_METERS), 0, RoundingMode.HALF_UP)

    override fun convertSquareMetersToSquareFeet(squareMeters: BigDecimal): BigDecimal =
        squareMeters.multiply(BigDecimal(SQUARE_FEET_TO_SQUARE_METERS)).setScale(0, RoundingMode.HALF_UP)

    override fun convertDollarToEuro(dollar: BigDecimal, currencyRate: BigDecimal): BigDecimal =
        dollar.divide(currencyRate, 0, RoundingMode.HALF_UP)

    override fun convertEuroToDollar(euro: BigDecimal, currencyRate: BigDecimal): BigDecimal =
        euro.multiply(currencyRate).setScale(0, RoundingMode.HALF_UP)


    override fun formatPriceToHumanReadable(price: BigDecimal): String {
        val roundedPrice = price.setScale(0, RoundingMode.HALF_UP)

        val numberFormat = NumberFormat.getCurrencyInstance(locale)
        if (numberFormat.currency?.symbol != "€" && numberFormat.currency?.symbol != "$") {
            return "$$roundedPrice"
        }
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