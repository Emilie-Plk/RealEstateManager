package com.emplk.realestatemanager.data.currency_rate

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.emplk.realestatemanager.data.DataModule
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
import java.time.LocalDateTime
import javax.inject.Inject


class CurrencyRateRepositoryFixer @Inject constructor(
    private val fixerApi: FixerApi,
    @DataModule.FixerApiDataStore private val dataStorePreferences: DataStore<Preferences>,
    private val clock: Clock,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : CurrencyRateRepository {

    companion object {
        private const val USD_TO_EURO_RATE_FALLBACK = 0.95
        private val USD_TO_EUR_RATE_KEY = stringPreferencesKey("usd_to_eur_rate")
        private val LAST_RATE_DATE_KEY = stringPreferencesKey("last_rate_timestamp")
    }

    override suspend fun getCurrentCurrencyRate(): CurrencyRateWrapper = withContext(coroutineDispatcherProvider.io) {
        val cachedCurrencyRate = getCachedCurrencyRateFlow().firstOrNull()
        // if cached currency rate is null or if it's older than 4 hours, we fetch a new one
        if (cachedCurrencyRate == null || cachedCurrencyRate.lastUpdatedDate.isBefore(
                LocalDateTime.now(clock).minusHours(4)
            )
        ) {
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
            dataStorePreferences.edit { preferences ->
                preferences[USD_TO_EUR_RATE_KEY] = currencyRateEntity.usdToEuroRate.toString()
                preferences[LAST_RATE_DATE_KEY] = currencyRateEntity.lastUpdatedDate.toString()
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    private suspend fun getCachedCurrencyRateFlow(): Flow<CurrencyRateEntity?> =
        dataStorePreferences.data
            .catch { exception ->
                exception.printStackTrace()
                emit(emptyPreferences())
            }
            .map { preferences ->
                val usdToEuroRate = preferences[USD_TO_EUR_RATE_KEY]
                val lastRateDate = preferences[LAST_RATE_DATE_KEY]
                if (usdToEuroRate != null && lastRateDate != null) {
                    Log.d(
                        "COUCOU",
                        "CurrencyRateRepositoryFixer getCachedCurrencyRateFlow: ${LocalDateTime.parse(lastRateDate)}"
                    )
                    CurrencyRateEntity(
                        usdToEuroRate = BigDecimal(usdToEuroRate),
                        lastUpdatedDate = LocalDateTime.parse(lastRateDate)
                    )
                } else null
            }

    private fun mapToCurrencyRateWrapper(response: FixerCurrencyRateResponse): CurrencyRateEntity? {
        return if (response.rateResponse?.usd != null) {
            CurrencyRateEntity(
                usdToEuroRate = BigDecimal(response.rateResponse.usd),
                lastUpdatedDate = LocalDateTime.now(clock)
            )
        } else null
    }
}