package com.emplk.realestatemanager.domain.filter

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId

class SearchedEntryDateRangeTest {

    companion object {
        private val clock = Clock.fixed(
            LocalDateTime.parse("2023-01-01T00:00:00").atZone(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
        )
    }

    @Test
    fun `nominal case`() {
        assertEquals(
            LocalDateTime.parse("2022-01-01T00:00:00"),
            SearchedEntryDateRange.LESS_THAN_1_YEAR.searchedEntryDateLambda(clock)?.withNano(0)
        )
        assertEquals(
            LocalDateTime.parse("2022-07-01T00:00:00"),
            SearchedEntryDateRange.LESS_THAN_6_MONTHS.searchedEntryDateLambda(clock)?.withNano(0)
        )
        assertEquals(
            LocalDateTime.parse("2022-10-01T00:00:00"),
            SearchedEntryDateRange.LESS_THAN_3_MONTHS.searchedEntryDateLambda(clock)?.withNano(0)
        )
        assertEquals(
            LocalDateTime.parse("2022-12-01T00:00:00"),
            SearchedEntryDateRange.LESS_THAN_1_MONTH.searchedEntryDateLambda(clock)?.withNano(0)
        )
        assertEquals(
            LocalDateTime.parse("2022-12-25T00:00:00"),
            SearchedEntryDateRange.LESS_THAN_1_WEEK.searchedEntryDateLambda(clock)?.withNano(0)
        )
        assertEquals(null, SearchedEntryDateRange.ALL.searchedEntryDateLambda(clock))
    }
}