package com.emplk.realestatemanager.data.currency

import android.content.res.Resources
import android.os.Build
import com.emplk.realestatemanager.domain.currency.CurrencyRepository
import com.emplk.realestatemanager.domain.currency.CurrencyType
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    val resources: Resources,
) : CurrencyRepository {
    override fun getLocaleCurrencyFormatting(): CurrencyType {
        @Suppress("DEPRECATION")
        val localeCountry = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0].country
        } else {
            resources.configuration.locale.country
        }
        return when (localeCountry) {
            "US" -> CurrencyType.DOLLAR
            "FR" -> CurrencyType.EURO
            else -> CurrencyType.DOLLAR
        }
    }
}