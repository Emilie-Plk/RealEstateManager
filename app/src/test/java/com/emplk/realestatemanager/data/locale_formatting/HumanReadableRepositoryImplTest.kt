package com.emplk.realestatemanager.data.locale_formatting

import com.emplk.realestatemanager.domain.locale_formatting.currency.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.surface.SurfaceUnitType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.util.Locale

class HumanReadableRepositoryImplTest {

    private val locale: Locale = Locale.US

    private val humanReadableRepositoryImpl = HumanReadableRepositoryImpl(locale)

    @Test
    fun `getLocale() - nominal case`() {
        // When
        val result = humanReadableRepositoryImpl.getLocale()

        // Then
        assertEquals(locale, result)
    }

    @Test
    fun `convertSquareFeetToSquareMetersRoundedHalfUp() - nominal case`() {
        // When
        val result = humanReadableRepositoryImpl.convertSquareFeetToSquareMetersRoundedHalfUp(BigDecimal(10))

        // Then
        assertEquals(BigDecimal.ONE, result)
    }

    @Test
    fun `convertSquareMetersToSquareFeetRoundedHalfUp() - nominal case`() {
        // When
        val result = humanReadableRepositoryImpl.convertSquareMetersToSquareFeetRoundedHalfUp(BigDecimal(10))

        // Then
        assertEquals(BigDecimal(108), result)
    }

    @Test
    fun `convertDollarToEuroRoundedHalfUp() - nominal case`() {
        // When
        val result = humanReadableRepositoryImpl.convertDollarToEuroRoundedHalfUp(BigDecimal(10), BigDecimal(2))

        // Then
        assertEquals(BigDecimal(5), result)
    }

    @Test
    fun `convertEuroToDollarRoundedHalfUp() - nominal case`() {
        // When
        val result = humanReadableRepositoryImpl.convertEuroToDollarRoundedHalfUp(BigDecimal(10), BigDecimal(2))

        // Then
        assertEquals(BigDecimal(20), result)
    }

    @Test
    fun `formatRoundedPriceToHumanReadable() - case Locale US`() {
        // When
        val result = humanReadableRepositoryImpl.formatRoundedPriceToHumanReadable(BigDecimal(10))

        // Then
        assertEquals("$10", result)
    }

    @Test
    fun `formatRoundedPriceToHumanReadable() - case Locale FR`() {
        // Given
        val frenchLocale = Locale.FRANCE
        val humanReadableRepositoryImpl = HumanReadableRepositoryImpl(frenchLocale)

        // When
        val result = humanReadableRepositoryImpl.formatRoundedPriceToHumanReadable(BigDecimal(10))

        // Then
        assertEquals("10 €", result)
    }

    @Test
    fun `formatRoundedPriceToHumanReadable() - case Locale other than US or FR`() {
        // Given
        val taiwaneseLocale = Locale.TAIWAN
        val humanReadableRepositoryImpl = HumanReadableRepositoryImpl(taiwaneseLocale)

        // When
        val result = humanReadableRepositoryImpl.formatRoundedPriceToHumanReadable(BigDecimal(10))

        // Then
        assertEquals("$10", result)
    }

    @Test
    fun `getLocaleCurrencyFormatting() - case Locale US`() {
        // When
        val result = humanReadableRepositoryImpl.getLocaleCurrencyFormatting()

        // Then
        assertEquals(CurrencyType.DOLLAR, result)
    }

    @Test
    fun `getLocaleCurrencyFormatting() - case Locale FR`() {
        // Given
        val frenchLocale = Locale.FRANCE
        val humanReadableRepositoryImpl = HumanReadableRepositoryImpl(frenchLocale)

        // When
        val result = humanReadableRepositoryImpl.getLocaleCurrencyFormatting()

        // Then
        assertEquals(CurrencyType.EURO, result)
    }

    @Test
    fun `getLocaleCurrencyFormatting() - case Locale other than US or FR`() {
        // Given
        val taiwaneseLocale = Locale.TAIWAN
        val humanReadableRepositoryImpl = HumanReadableRepositoryImpl(taiwaneseLocale)

        // When
        val result = humanReadableRepositoryImpl.getLocaleCurrencyFormatting()

        // Then
        assertEquals(CurrencyType.DOLLAR, result)
    }

    @Test
    fun `getLocaleSurfaceUnitFormatting() - case Locale US`() {
        // When
        val result = humanReadableRepositoryImpl.getLocaleSurfaceUnitFormatting()

        // Then
        assertEquals(SurfaceUnitType.SQUARE_FOOT, result)
    }

    @Test
    fun `getLocaleSurfaceUnitFormatting() - case Locale FR`() {
        // Given
        val frenchLocale = Locale.FRANCE
        val humanReadableRepositoryImpl = HumanReadableRepositoryImpl(frenchLocale)

        // When
        val result = humanReadableRepositoryImpl.getLocaleSurfaceUnitFormatting()

        // Then
        assertEquals(SurfaceUnitType.SQUARE_METER, result)
    }

    @Test
    fun `getLocaleSurfaceUnitFormatting() - case Locale other than US or FR`() {
        // Given
        val taiwaneseLocale = Locale.TAIWAN
        val humanReadableRepositoryImpl = HumanReadableRepositoryImpl(taiwaneseLocale)

        // When
        val result = humanReadableRepositoryImpl.getLocaleSurfaceUnitFormatting()

        // Then
        assertEquals(SurfaceUnitType.SQUARE_FOOT, result)
    }
}