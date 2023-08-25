package com.emplk.realestatemanager.data.currency

import android.content.res.Resources
import android.os.Build
import com.emplk.realestatemanager.domain.locale_formatting.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.LocaleFormattingRepository
import com.emplk.realestatemanager.domain.locale_formatting.SurfaceUnitType
import javax.inject.Inject

class LocaleFormattingRepositoryImpl @Inject constructor(
    val resources: Resources,
) : LocaleFormattingRepository {

    override fun getLocaleCountry(): String =
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0].country
        } else {
            resources.configuration.locale.country
        }

    override fun getLocaleCurrencyFormatting(): CurrencyType = when (getLocaleCountry()) {
        "US" -> CurrencyType.DOLLAR
        "FR" -> CurrencyType.EURO
        else -> CurrencyType.DOLLAR
    }

    override fun getLocaleSurfaceUnitFormatting(): SurfaceUnitType = when (getLocaleCountry()) {
        "US" -> SurfaceUnitType.SQUARE_FEET
        "FR" -> SurfaceUnitType.SQUARE_METER
        else -> SurfaceUnitType.SQUARE_FEET
    }
}