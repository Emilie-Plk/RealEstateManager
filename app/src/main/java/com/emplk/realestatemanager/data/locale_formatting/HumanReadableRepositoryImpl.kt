package com.emplk.realestatemanager.data.locale_formatting

import com.emplk.realestatemanager.domain.locale_formatting.HumanReadableRepository
import com.emplk.realestatemanager.domain.locale_formatting.currency.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.surface.SurfaceUnitType
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

class HumanReadableRepositoryImpl @Inject constructor(
    private val locale: Locale,
) : HumanReadableRepository {

    companion object {
        private const val SQUARE_FEET_TO_SQUARE_METERS = 10.7639
    }

    override fun getLocale(): Locale = locale

    override fun convertSquareFeetToSquareMetersRoundedHalfUp(squareFeet: BigDecimal): BigDecimal =
        squareFeet.divide(BigDecimal(SQUARE_FEET_TO_SQUARE_METERS), 0, RoundingMode.HALF_UP)

    override fun convertSquareMetersToSquareFeetRoundedHalfUp(squareMeters: BigDecimal): BigDecimal =
        squareMeters.multiply(BigDecimal(SQUARE_FEET_TO_SQUARE_METERS)).setScale(0, RoundingMode.HALF_UP)

    override fun convertDollarToEuroRoundedHalfUp(dollar: BigDecimal, currencyRate: BigDecimal): BigDecimal =
        dollar.divide(currencyRate, 0, RoundingMode.HALF_UP)

    override fun convertEuroToDollarRoundedHalfUp(euro: BigDecimal, currencyRate: BigDecimal): BigDecimal =
        euro.multiply(currencyRate).setScale(0, RoundingMode.HALF_UP)

    // TODO: formatRoundedSurfaceToHumanReadable?
    fun formatRoundedSurfaceToHumanReadable(surface: BigDecimal): String {
        val unitOfmeasure = getLocaleSurfaceUnitFormatting()
        return when (unitOfmeasure) {
            SurfaceUnitType.SQUARE_METER -> "$surface m²"
            SurfaceUnitType.SQUARE_FOOT -> "${convertSquareMetersToSquareFeetRoundedHalfUp(surface)} ft²"
        }
    }

    override fun formatRoundedPriceToHumanReadable(price: BigDecimal): String {
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