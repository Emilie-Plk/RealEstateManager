package com.emplk.realestatemanager.domain.filter

import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class ConvertEntryDateStateToEpochMilliUseCase @Inject constructor(private val clock: Clock) {
    fun invoke(entryDateState: EntryDateState): Long? {
        val now = LocalDateTime.now(clock)
        val entryDateMin = entryDateState.entryDateLambda.invoke(clock)

        return if (entryDateMin == null) null
        else return when (entryDateState) {
            EntryDateState.LESS_THAN_1_YEAR ->
                entryDateMin.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            EntryDateState.LESS_THAN_6_MONTHS ->
                now.minusMonths(6).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            EntryDateState.LESS_THAN_3_MONTHS ->
                now.minusMonths(3).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            EntryDateState.LESS_THAN_1_MONTH ->
                now.minusMonths(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            EntryDateState.LESS_THAN_1_WEEK ->
                now.minusWeeks(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            EntryDateState.ALL -> null
        }
    }
}