package com.emplk.realestatemanager.data.currency_rate

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.emplk.realestatemanager.data.api.FixerApi
import com.emplk.realestatemanager.data.currency_rate.response.FixerCurrencyRateResponse
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.currency_rate.CurrencyRateEntity
import com.emplk.realestatemanager.domain.currency_rate.CurrencyRateRepository
import com.emplk.realestatemanager.domain.currency_rate.CurrencyRateWrapper
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class CurrencyRateRepositoryFixer @Inject constructor(
    private val fixerApi: FixerApi,
    private val application: Application,
    private val clock: Clock,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : CurrencyRateRepository {

    companion object {
        private const val USD_TO_EURO_RATE_FALLBACK = 0.95
        private const val CURRENCY_RATE = "currency_rate"

        private val USD_TO_EUR_RATE_KEY = stringPreferencesKey("usd_to_eur_rate")
        private val LAST_RATE_DATE_KEY = stringPreferencesKey("last_rate_date")
    }

    private val Context.currencyRateDatastore: DataStore<Preferences> by preferencesDataStore(name = CURRENCY_RATE)

    override suspend fun getCurrentCurrencyRate(): CurrencyRateWrapper = withContext(coroutineDispatcherProvider.io) {
        val cachedCurrencyRate = getCachedCurrencyRateFlow().firstOrNull()
        if (cachedCurrencyRate == null || cachedCurrencyRate.lastUpdatedDate != LocalDate.now(clock)) {
            try {
                val response: FixerCurrencyRateResponse = fixerApi.getLatestCurrencyRates()
                if (response.success == true && response.rateResponse != null) {
                    val currencyRateEntity = mapToCurrencyRateWrapper(response)
                    if (currencyRateEntity != null) {
                        updateCachedCurrencyRate(currencyRateEntity)
                        CurrencyRateWrapper.Success(currencyRateEntity)
                    } else CurrencyRateWrapper.Error(BigDecimal(USD_TO_EURO_RATE_FALLBACK))
                } else CurrencyRateWrapper.Error(BigDecimal(USD_TO_EURO_RATE_FALLBACK))
            } catch (exception: Exception) {
                coroutineContext.ensureActive()
                CurrencyRateWrapper.Error(BigDecimal(USD_TO_EURO_RATE_FALLBACK))
            }
        } else CurrencyRateWrapper.Success(cachedCurrencyRate)
    }


    private suspend fun updateCachedCurrencyRate(currencyRateEntity: CurrencyRateEntity) {
        try {
            application.currencyRateDatastore.edit { preferences ->
                preferences[USD_TO_EUR_RATE_KEY] = currencyRateEntity.usdToEuroRate.toString()
                preferences[LAST_RATE_DATE_KEY] = currencyRateEntity.lastUpdatedDate.toString()
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    private suspend fun getCachedCurrencyRateFlow(): Flow<CurrencyRateEntity?> =
        application.currencyRateDatastore.data
            .catch { exception ->
                exception.printStackTrace()
                emit(emptyPreferences())
            }
            .map { preferences ->
                val usdToEuroRate = preferences[USD_TO_EUR_RATE_KEY]
                val lastRateDate = preferences[LAST_RATE_DATE_KEY]
                if (usdToEuroRate != null && lastRateDate != null) {
                    CurrencyRateEntity(
                        usdToEuroRate = BigDecimal(usdToEuroRate),
                        lastUpdatedDate = LocalDate.parse(lastRateDate)
                    )
                } else null
            }

    private fun mapToCurrencyRateWrapper(response: FixerCurrencyRateResponse): CurrencyRateEntity? {
        return if (response.rateResponse?.usd != null &&
            response.date != null
        ) {
            CurrencyRateEntity(
                usdToEuroRate = BigDecimal(response.rateResponse.usd),
                lastUpdatedDate = LocalDate.parse(response.date, DateTimeFormatter.ISO_DATE)
            )
        } else null
    }
}