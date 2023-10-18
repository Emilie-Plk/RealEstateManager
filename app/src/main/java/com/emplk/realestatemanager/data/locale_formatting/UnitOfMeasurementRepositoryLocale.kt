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

class UnitOfMeasurementRepositoryLocale @Inject constructor(
    private val locale: Locale,
) : LocaleFormattingRepository {

    companion object {
        private const val SQUARE_FEET_TO_SQUARE_METERS = 10.764
    }

    override fun convertSquareFeetToSquareMeters(squareFeet: BigDecimal): BigDecimal =
        squareFeet.divide(BigDecimal(SQUARE_FEET_TO_SQUARE_METERS),0, RoundingMode.HALF_UP)

    override fun convertSquareMetersToSquareFeet(squareMeters: BigDecimal): BigDecimal =
        squareMeters.multiply(BigDecimal(SQUARE_FEET_TO_SQUARE_METERS)).setScale(0, RoundingMode.HALF_UP)

    override fun convertDollarToEuro(dollar: BigDecimal, currencyRate: Double): BigDecimal =
        dollar.multiply(BigDecimal(currencyRate)).setScale(0, RoundingMode.HALF_UP)

    override fun convertEuroToDollar(euro: BigDecimal, currencyRate: Double): BigDecimal =
        euro.divide(BigDecimal(currencyRate), 0, RoundingMode.HALF_UP)

    override fun getLocale(): Locale = locale

    override fun formatPrice(price: BigDecimal): String {
        val roundedPrice = price.setScale(0, RoundingMode.HALF_UP)

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