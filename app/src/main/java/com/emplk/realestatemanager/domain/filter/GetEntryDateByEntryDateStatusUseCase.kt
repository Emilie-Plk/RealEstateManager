package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.ui.filter.EntryDateState
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class GetEntryDateByEntryDateStatusUseCase @Inject constructor(private val clock: Clock) {
    fun invoke(entryDateState: EntryDateState): Long? {
        val now = LocalDateTime.now(clock)
        return when (entryDateState) {

            EntryDateState.LESS_THAN_1_YEAR ->
                now.minusYears(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() // 1 year ago min

            EntryDateState.LESS_THAN_6_MONTHS ->
                now.minusMonths(6).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() // 6 months ago min

            EntryDateState.LESS_THAN_3_MONTHS ->
                now.minusMonths(3).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() // 3 months ago  min

            EntryDateState.LESS_THAN_1_MONTH ->
                now.minusMonths(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            EntryDateState.LESS_THAN_1_WEEK ->
                now.minusWeeks(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            EntryDateState.ALL -> null
        }
    }
}