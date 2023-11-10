package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.ui.filter.EntryDateState
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class GetEntryDateByEntryDateStatusUseCase @Inject constructor(private val clock: Clock) {
    fun invoke(entryDateState: EntryDateState): Pair<Long?, Long?>? {
        val now = LocalDateTime.now(clock)
        return when (entryDateState) {

            EntryDateState.LESS_THAN_1_YEAR -> Pair(
                now.minusYears(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), // 1 year ago min
                now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), // now max
            )

            EntryDateState.LESS_THAN_6_MONTHS -> Pair(
                now.minusMonths(6).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), // 6 months ago min
                now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), // now max
            )

            EntryDateState.LESS_THAN_3_MONTHS -> Pair(
                now.minusMonths(3).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), // 3 months ago  min
                now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), // now max
            )

            EntryDateState.LESS_THAN_1_MONTH -> Pair(
                now.minusMonths(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            )

            EntryDateState.LESS_THAN_1_WEEK -> Pair(
                now.minusWeeks(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            )

            EntryDateState.NONE -> null
        }
    }
}