package com.emplk.realestatemanager.domain.currency_rate

import com.emplk.realestatemanager.domain.locale_formatting.GetLocaleUseCase
import com.emplk.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Locale

class ConvertPriceDependingOnLocaleUseCaseTest {
    companion object {
        private val TEST_USD_TO_EURO_RATE = BigDecimal(0.92)
        private val TEST_PRICE = BigDecimal(100)
        private val TEST_PRICE_CONVERTED = BigDecimal(93)
        private val LAST_UPDATED_LOCAL_DATE_TIME = LocalDateTime.of(2023, 12, 4, 13, 0)

    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val getCurrencyRateUseCase: GetCurrencyRateUseCase = mockk()
    private val convertDollarToEuroUseCase: ConvertDollarToEuroUseCase = mockk()
    private val getLocaleUseCase: GetLocaleUseCase = mockk()

    private val convertPriceDependingOnLocaleUseCase = ConvertPriceDependingOnLocaleUseCase(
        getCurrencyRateUseCase,
        convertDollarToEuroUseCase,
        getLocaleUseCase
    )

    @Before
    fun setUp() {
        coEvery { getCurrencyRateUseCase.invoke() } returns CurrencyRateWrapper.Success(
            CurrencyRateEntity(
                usdToEuroRate = TEST_USD_TO_EURO_RATE,
                lastUpdatedDate = LAST_UPDATED_LOCAL_DATE_TIME
            )
        )

        coEvery { convertDollarToEuroUseCase.invoke(TEST_PRICE, TEST_USD_TO_EURO_RATE) } returns TEST_PRICE_CONVERTED

        every { getLocaleUseCase.invoke() } returns Locale.US
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // When
        val result = convertPriceDependingOnLocaleUseCase.invoke(TEST_PRICE)

        // Then
        verify(exactly = 1) { getLocaleUseCase.invoke() }
        assert(result == TEST_PRICE)
        confirmVerified(getLocaleUseCase)
    }

    @Test
    fun `locale is France with CurrencyRateWrapper is Success`() = testCoroutineRule.runTest {
        // Given
        every { getLocaleUseCase.invoke() } returns Locale.FRANCE

        // When
        val result = convertPriceDependingOnLocaleUseCase.invoke(TEST_PRICE)

        // Then
        verify(exactly = 1) {
            getLocaleUseCase.invoke()
            convertDollarToEuroUseCase.invoke(TEST_PRICE, TEST_USD_TO_EURO_RATE)
        }
        coVerify(exactly = 1) { getCurrencyRateUseCase.invoke() }
        assert(result == TEST_PRICE_CONVERTED)
        confirmVerified(getLocaleUseCase, convertDollarToEuroUseCase, getCurrencyRateUseCase)
    }

    @Test
    fun `locale is France with CurrencyRateWrapper is Error`() = testCoroutineRule.runTest {
        // Given
        every { getLocaleUseCase.invoke() } returns Locale.FRANCE
        coEvery { getCurrencyRateUseCase.invoke() } returns CurrencyRateWrapper.Error(
            fallbackUsToEuroRate = TEST_USD_TO_EURO_RATE
        )

        // When
        val result = convertPriceDependingOnLocaleUseCase.invoke(TEST_PRICE)

        // Then
        verify(exactly = 1) {
            getLocaleUseCase.invoke()
            convertDollarToEuroUseCase.invoke(TEST_PRICE, TEST_USD_TO_EURO_RATE)
        }
        coVerify(exactly = 1) { getCurrencyRateUseCase.invoke() }
        assert(result == TEST_PRICE_CONVERTED)
        confirmVerified(getLocaleUseCase, convertDollarToEuroUseCase, getCurrencyRateUseCase)
    }
}