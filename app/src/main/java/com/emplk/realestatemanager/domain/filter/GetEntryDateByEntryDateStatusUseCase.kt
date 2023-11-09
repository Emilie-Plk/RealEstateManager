package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.ui.filter.EntryDateState
import java.time.Clock
import java.time.LocalDateTime
import javax.inject.Inject

class GetEntryDateByEntryDateStatusUseCase @Inject constructor(
    private val clock: Clock,
) {

    fun invoke(entryDateState: EntryDateState): Pair<LocalDateTime, LocalDateTime>? = when (entryDateState) {
        EntryDateState.MORE_THAN_6_MONTHS -> Pair(LocalDateTime.now(clock), LocalDateTime.now(clock).minusMonths(6))
        EntryDateState.LESS_THAN_3_MONTHS -> Pair(
            LocalDateTime.now(clock).minusMonths(6),
            LocalDateTime.now(clock).minusMonths(3)
        )

        EntryDateState.LESS_THAN_1_MONTH -> Pair(
            LocalDateTime.now(clock).minusMonths(3),
            LocalDateTime.now(clock).minusMonths(1)
        )

        EntryDateState.LESS_THAN_1_WEEK -> Pair(
            LocalDateTime.now(clock).minusMonths(1),
            LocalDateTime.now(clock).minusWeeks(1)
        )

        EntryDateState.NONE -> null
    }
}