package com.emplk.realestatemanager.domain.filter

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId

class EntryDateStateTest {

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
            EntryDateState.LESS_THAN_1_YEAR.entryDateLambda(clock)?.withNano(0)
        )
        assertEquals(
            LocalDateTime.parse("2022-07-01T00:00:00"),
            EntryDateState.LESS_THAN_6_MONTHS.entryDateLambda(clock)?.withNano(0)
        )
        assertEquals(
            LocalDateTime.parse("2022-10-01T00:00:00"),
            EntryDateState.LESS_THAN_3_MONTHS.entryDateLambda(clock)?.withNano(0)
        )
        assertEquals(
            LocalDateTime.parse("2022-12-01T00:00:00"),
            EntryDateState.LESS_THAN_1_MONTH.entryDateLambda(clock)?.withNano(0)
        )
        assertEquals(
            LocalDateTime.parse("2022-12-25T00:00:00"),
            EntryDateState.LESS_THAN_1_WEEK.entryDateLambda(clock)?.withNano(0)
        )
        assertEquals(null, EntryDateState.ALL.entryDateLambda(clock))
    }
}