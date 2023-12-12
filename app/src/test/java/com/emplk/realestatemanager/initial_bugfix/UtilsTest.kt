package com.emplk.realestatemanager.initial_bugfix;

import com.emplk.realestatemanager.initial_bugfix.Utils.convertDollarToEuro
import com.emplk.realestatemanager.initial_bugfix.Utils.convertEuroToDollar
import com.emplk.realestatemanager.initial_bugfix.Utils.getTodayDate
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class UtilsTest {

    companion object {
        private const val TEST_DOLLAR_AMOUNT = 1000000
        private const val TEST_EURO_AMOUNT = 1000000
        private const val TEST_EURO_RESULT = 812000
        private const val TEST_DOLLAR_RESULT = 1230000
    }

    @Test
    fun `dollar to euro conversion`() {
        val result = convertDollarToEuro(TEST_DOLLAR_AMOUNT)
        assertEquals(TEST_EURO_RESULT, result)
    }

    @Test
    fun `euro to dollar conversion`() {
        val result = convertEuroToDollar(TEST_EURO_AMOUNT)
        assertEquals(TEST_DOLLAR_RESULT, result)
    }

    @Test
    fun `date should be parsed according to locale - US`() {
        // Given
        Locale.setDefault(Locale.US)
        val today = Date()
        val expectedDateFormatUs = SimpleDateFormat("MM/dd/yy", Locale.US)
        val expected = expectedDateFormatUs.format(today)

        // When
        val result = getTodayDate()
        val locale = Locale.getDefault()
        println("Locale: $locale - Date: $result")

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun `date should be parsed according to locale - FR`() {
        // Given
        Locale.setDefault(Locale.FRANCE)
        val today = Date()
        val expectedDateFormatFr = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        val expected = expectedDateFormatFr.format(today)

        // When
        val result = getTodayDate()
        val locale = Locale.getDefault()
        println("Locale: $locale - Date: $result")

        // Then
        assertEquals(expected, result)
    }
}