package com.emplk.realestatemanager.data.currency_rate

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.emplk.realestatemanager.data.api.FixerApi
import com.emplk.realestatemanager.data.currency_rate.response.FixerCurrencyRateResponse
import com.emplk.realestatemanager.data.currency_rate.response.RateResponse
import com.emplk.realestatemanager.domain.currency_rate.CurrencyRateEntity
import com.emplk.realestatemanager.domain.currency_rate.CurrencyRateWrapper
import com.emplk.realestatemanager.fixtures.testFixedClock
import com.emplk.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class CurrencyRateRepositoryFixerTest {
    companion object {
        private const val USD_TO_EURO_RATE_FALLBACK = 0.95
        private val TEST_USD_TO_EUR_RATE_KEY = stringPreferencesKey("usd_to_eur_rate")
        private val TEST_LAST_RATE_DATE_KEY = stringPreferencesKey("last_rate_timestamp")
        private const val TEST_USD_TO_EUR_RATE = "1.07"
        private val TEST_LAST_RATE_DATE = (LocalDateTime.of(2023, 10, 31, 13, 22, 35)).toString()
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()


    private val testDataStore: DataStore<Preferences> = mockk()

    private val mockedPreferences = mockk<Preferences>()

    private val fixerApi: FixerApi = mockk()

    private val currencyRateRepositoryFixer =
        CurrencyRateRepositoryFixer(
            fixerApi,
            testDataStore,
            testFixedClock,
            testCoroutineRule.getTestCoroutineDispatcherProvider(),
        )

    @Before
    fun setUp() {
        coEvery { fixerApi.getLatestCurrencyRates() } returns getTestFixerCurrencyRateResponse()
        coEvery { testDataStore.data } returns flowOf(mockedPreferences)
        coEvery { mockedPreferences[TEST_USD_TO_EUR_RATE_KEY] } returns TEST_USD_TO_EUR_RATE
        coEvery { mockedPreferences[TEST_LAST_RATE_DATE_KEY] } returns TEST_LAST_RATE_DATE
        coEvery {
            testDataStore.edit { preferences ->
                preferences[TEST_USD_TO_EUR_RATE_KEY] = TEST_USD_TO_EUR_RATE
                preferences[TEST_LAST_RATE_DATE_KEY] = TEST_LAST_RATE_DATE
            }
        } returns mockedPreferences

        coEvery { testDataStore.updateData { any() } } coAnswers {
            mockedPreferences
        }
    }


    @Test
    fun `getCurrentCurrencyRate - nominal case`() = testCoroutineRule.runTest {
        // When
        val result = currencyRateRepositoryFixer.getCurrentCurrencyRate()
        // Then
        assertThat(result).isEqualTo(CurrencyRateWrapper.Success(getTestCurrencyRateEntity()))
    }

    @Test
    fun `currency has changed`() = testCoroutineRule.runTest {
        // Given
        val currencyRateResponseChanged = getTestFixerCurrencyRateResponse().copy(
            rateResponse = RateResponse(
                usd = "1.15", // changed from 1.07 to 1.15)
            )
        )
        val currencyRateEntityChanged = getTestCurrencyRateEntity().copy(
            usdToEuroRate = BigDecimal("1.15"),
            lastUpdatedDate = LocalDateTime.of(2023, 10, 31, 13, 22, 35),
        )

        coEvery { fixerApi.getLatestCurrencyRates() } returns currencyRateResponseChanged

        coEvery { testDataStore.data } returns emptyFlow()

        val mockedPreferencesChanged = mockk<Preferences>()

        coEvery {
            testDataStore.updateData {
                mockedPreferencesChanged
            }
        }

        coEvery {
            testDataStore.edit { preferences ->
                preferences[TEST_USD_TO_EUR_RATE_KEY] = "1.15"
                preferences[TEST_LAST_RATE_DATE_KEY] = LocalDateTime.of(2023, 10, 31, 13, 22, 35).toString()
            }
        } returns mockedPreferencesChanged

        coEvery { mockedPreferencesChanged[TEST_USD_TO_EUR_RATE_KEY] } returns "1.15"
        coEvery { mockedPreferencesChanged[TEST_LAST_RATE_DATE_KEY] } returns LocalDateTime.of(2023, 10, 31, 13, 22, 35)
            .toString()


        // When
        val result = currencyRateRepositoryFixer.getCurrentCurrencyRate()
        runCurrent()
        // Then
        assertThat(result).isEqualTo(CurrencyRateWrapper.Success(currencyRateEntityChanged))
    }


}


fun getTestFixerCurrencyRateResponse() = FixerCurrencyRateResponse(
    date = "2021-10-31",
    success = true,
    timestamp = 1635686399,
    base = "EUR",
    rateResponse = RateResponse(
        usd = "1.07", // as of 31 Oct 2023
    )
)

fun getTestCurrencyRateEntity() = CurrencyRateEntity(
    usdToEuroRate = BigDecimal("1.07"),
    lastUpdatedDate = LocalDateTime.of(2023, 10, 31, 13, 22, 35),
)