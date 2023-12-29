package com.emplk.realestatemanager.utils;

import com.emplk.realestatemanager.ui.utils.Utils.convertDollarToEuro
import com.emplk.realestatemanager.ui.utils.Utils.convertEuroToDollar
import com.emplk.realestatemanager.ui.utils.Utils.getTodayDate
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
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

    private lateinit var locale: Locale

    @Before
    fun setUp() {
        locale = Locale.getDefault()
    }

    @After
    fun tearDown() {
        Locale.setDefault(locale)
    }


    @Test
    fun `dollar to euro conversion`() {
        // When
        val result = convertDollarToEuro(TEST_DOLLAR_AMOUNT)

        // Then
        assertEquals(TEST_EURO_RESULT, result)
    }

    @Test
    fun `euro to dollar conversion`() {
        // When
        val result = convertEuroToDollar(TEST_EURO_AMOUNT)

        // Then
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

        // Then
        assertEquals(expected, result)
    }
}