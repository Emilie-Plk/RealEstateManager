package com.emplk.realestatemanager.data.locale_formatting

import com.emplk.realestatemanager.domain.locale_formatting.currency.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.surface.SurfaceUnitType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.util.Locale

class LocaleFormattingRepositoryImplTest {

    private val locale: Locale = Locale.US

    private val localeFormattingRepositoryImpl = LocaleFormattingRepositoryImpl(locale)

    @Test
    fun `getLocale() - nominal case`() {
        // When
        val result = localeFormattingRepositoryImpl.getLocale()

        // Then
        assertEquals(locale, result)
    }

    @Test
    fun `convertSquareFeetToSquareMetersRoundedHalfUp() - nominal case`() {
        // When
        val result = localeFormattingRepositoryImpl.convertSquareFeetToSquareMetersRoundedHalfUp(BigDecimal(10))

        // Then
        assertEquals(BigDecimal.ONE, result)
    }

    @Test
    fun `convertSquareMetersToSquareFeetRoundedHalfUp() - nominal case`() {
        // When
        val result = localeFormattingRepositoryImpl.convertSquareMetersToSquareFeetRoundedHalfUp(BigDecimal(10))

        // Then
        assertEquals(BigDecimal(108), result)
    }

    @Test
    fun `convertDollarToEuroRoundedHalfUp() - nominal case`() {
        // When
        val result = localeFormattingRepositoryImpl.convertDollarToEuroRoundedHalfUp(BigDecimal(10), BigDecimal(2))

        // Then
        assertEquals(BigDecimal(5), result)
    }

    @Test
    fun `convertEuroToDollarRoundedHalfUp() - nominal case`() {
        // When
        val result = localeFormattingRepositoryImpl.convertEuroToDollarRoundedHalfUp(BigDecimal(10), BigDecimal(2))

        // Then
        assertEquals(BigDecimal(20), result)
    }

    @Test
    fun `formatRoundedPriceToHumanReadable() - case Locale US`() {
        // When
        val result = localeFormattingRepositoryImpl.formatRoundedPriceToHumanReadable(BigDecimal(10))

        // Then
        assertEquals("$10", result)
    }

    @Test
    fun `formatRoundedPriceToHumanReadable() - case Locale FR`() {
        // Given
        val frenchLocale = Locale.FRANCE
        val localeFormattingRepositoryImpl = LocaleFormattingRepositoryImpl(frenchLocale)

        // When
        val result = localeFormattingRepositoryImpl.formatRoundedPriceToHumanReadable(BigDecimal(10))

        // Then
        assertEquals("10 €", result)
    }

    @Test
    fun `formatRoundedPriceToHumanReadable() - case Locale other than US or FR`() {
        // Given
        val taiwaneseLocale = Locale.TAIWAN
        val localeFormattingRepositoryImpl = LocaleFormattingRepositoryImpl(taiwaneseLocale)

        // When
        val result = localeFormattingRepositoryImpl.formatRoundedPriceToHumanReadable(BigDecimal(10))

        // Then
        assertEquals("$10", result)
    }

    @Test
    fun `getLocaleCurrencyFormatting() - case Locale US`() {
        // When
        val result = localeFormattingRepositoryImpl.getLocaleCurrencyFormatting()

        // Then
        assertEquals(CurrencyType.DOLLAR, result)
    }

    @Test
    fun `getLocaleCurrencyFormatting() - case Locale FR`() {
        // Given
        val frenchLocale = Locale.FRANCE
        val localeFormattingRepositoryImpl = LocaleFormattingRepositoryImpl(frenchLocale)

        // When
        val result = localeFormattingRepositoryImpl.getLocaleCurrencyFormatting()

        // Then
        assertEquals(CurrencyType.EURO, result)
    }

    @Test
    fun `getLocaleCurrencyFormatting() - case Locale other than US or FR`() {
        // Given
        val taiwaneseLocale = Locale.TAIWAN
        val localeFormattingRepositoryImpl = LocaleFormattingRepositoryImpl(taiwaneseLocale)

        // When
        val result = localeFormattingRepositoryImpl.getLocaleCurrencyFormatting()

        // Then
        assertEquals(CurrencyType.DOLLAR, result)
    }

    @Test
    fun `getLocaleSurfaceUnitFormatting() - case Locale US`() {
        // When
        val result = localeFormattingRepositoryImpl.getLocaleSurfaceUnitFormatting()

        // Then
        assertEquals(SurfaceUnitType.SQUARE_FOOT, result)
    }

    @Test
    fun `getLocaleSurfaceUnitFormatting() - case Locale FR`() {
        // Given
        val frenchLocale = Locale.FRANCE
        val localeFormattingRepositoryImpl = LocaleFormattingRepositoryImpl(frenchLocale)

        // When
        val result = localeFormattingRepositoryImpl.getLocaleSurfaceUnitFormatting()

        // Then
        assertEquals(SurfaceUnitType.SQUARE_METER, result)
    }

    @Test
    fun `getLocaleSurfaceUnitFormatting() - case Locale other than US or FR`() {
        // Given
        val taiwaneseLocale = Locale.TAIWAN
        val localeFormattingRepositoryImpl = LocaleFormattingRepositoryImpl(taiwaneseLocale)

        // When
        val result = localeFormattingRepositoryImpl.getLocaleSurfaceUnitFormatting()

        // Then
        assertEquals(SurfaceUnitType.SQUARE_FOOT, result)
    }
}