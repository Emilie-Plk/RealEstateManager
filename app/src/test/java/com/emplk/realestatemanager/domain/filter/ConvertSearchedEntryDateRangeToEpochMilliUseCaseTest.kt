package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.fixtures.testFixedClock
import junit.framework.TestCase.assertNull
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Clock

class ConvertSearchedEntryDateRangeToEpochMilliUseCaseTest {

    companion object {
        private const val TEST_FIXED_EPOCH_MILLI_MINUS_1_YEAR = 1667222555000L //  Monday 31 October 2022 13:22:35
        private const val TEST_FIXED_EPOCH_MILLI_MINUS_6_MONTHS = 1682860955000L // Sunday 30 April 2023 13:22:35
        private const val TEST_FIXED_EPOCH_MILLI_MINUS_3_MONTHS = 1690809755000L //  Monday 31 July 2023 13:22:35
        private const val TEST_FIXED_EPOCH_MILLI_MINUS_1_MONTH = 1696080155000L // Saturday 30 September 2023 13:22:35
        private const val TEST_FIXED_EPOCH_MILLI_MINUS_1_WEEK = 1698153755000L // Tuesday 24 October 2023 13:22:35
    }

    private val clock: Clock = testFixedClock

    private val convertSearchedEntryDateRangeToEpochMilliUseCase =
        ConvertSearchedEntryDateRangeToEpochMilliUseCase(clock)

    @Test
    fun `invoke() should return null when entryDateState is ALL`() {
        // Given
        val searchedEntryDateRange = SearchedEntryDateRange.ALL

        // When
        val result = convertSearchedEntryDateRangeToEpochMilliUseCase.invoke(searchedEntryDateRange)

        // Then
        assertNull(result)
    }

    @Test
    fun `invoke() should return epochMilli when entryDateState is LESS_THAN_1_YEAR`() {
        // Given
        val searchedEntryDateRange = SearchedEntryDateRange.LESS_THAN_1_YEAR

        // When
        val result = convertSearchedEntryDateRangeToEpochMilliUseCase.invoke(searchedEntryDateRange)
        // Then
        assertEquals(TEST_FIXED_EPOCH_MILLI_MINUS_1_YEAR, result)
    }

    @Test
    fun `invoke() should return epochMilli when entryDateState is LESS_THAN_6_MONTHS`() {
        // Given
        val searchedEntryDateRange = SearchedEntryDateRange.LESS_THAN_6_MONTHS

        // When
        val result = convertSearchedEntryDateRangeToEpochMilliUseCase.invoke(searchedEntryDateRange)

        // Then
        assertEquals(TEST_FIXED_EPOCH_MILLI_MINUS_6_MONTHS, result)
    }

    @Test
    fun `invoke() should return epochMilli when entryDateState is LESS_THAN_3_MONTHS`() {
        // Given
        val searchedEntryDateRange = SearchedEntryDateRange.LESS_THAN_3_MONTHS

        // When
        val result = convertSearchedEntryDateRangeToEpochMilliUseCase.invoke(searchedEntryDateRange)

        // Then
        assertEquals(TEST_FIXED_EPOCH_MILLI_MINUS_3_MONTHS, result)
    }

    @Test
    fun `invoke() should return epochMilli when entryDateState is LESS_THAN_1_MONTH`() {
        // Given
        val searchedEntryDateRange = SearchedEntryDateRange.LESS_THAN_1_MONTH

        // When
        val result = convertSearchedEntryDateRangeToEpochMilliUseCase.invoke(searchedEntryDateRange)

        // Then
        assertEquals(TEST_FIXED_EPOCH_MILLI_MINUS_1_MONTH, result)
    }

    @Test
    fun `invoke() should return epochMilli when entryDateState is LESS_THAN_1_WEEK`() {
        // Given
        val searchedEntryDateRange = SearchedEntryDateRange.LESS_THAN_1_WEEK

        // When
        val result = convertSearchedEntryDateRangeToEpochMilliUseCase.invoke(searchedEntryDateRange)

        // Then
        assertEquals(TEST_FIXED_EPOCH_MILLI_MINUS_1_WEEK, result)
    }
}