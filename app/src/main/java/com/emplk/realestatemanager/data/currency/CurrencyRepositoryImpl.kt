package com.emplk.realestatemanager.data.currency

import android.content.res.Resources
import android.os.Build
import com.emplk.realestatemanager.domain.currency.CurrencyRepository
import com.emplk.realestatemanager.domain.currency.CurrencyType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    val resources: Resources,
) : CurrencyRepository {
    override fun getLocaleCurrencyFormatting(): Flow<CurrencyType> {
        return flow {
            val localeCountry =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    resources.configuration.locales[0].country
                } else {
                    resources.configuration.locale.country
                }
            when (localeCountry) {
                "US" -> emit(CurrencyType.DOLLAR)
                "FR" -> emit(CurrencyType.EURO)
                else -> emit(CurrencyType.DOLLAR)
            }
        }
    }
}