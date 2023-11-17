package com.emplk.realestatemanager.domain.filter

import java.time.Clock
import java.time.LocalDateTime

enum class EntryDateState(
    val entryDateLambda: (Clock) -> LocalDateTime?
) {
    LESS_THAN_1_YEAR({ clock -> LocalDateTime.now(clock).minusYears(1) }),
    LESS_THAN_6_MONTHS({ clock -> LocalDateTime.now(clock).minusMonths(6) }),
    LESS_THAN_3_MONTHS({ clock -> LocalDateTime.now(clock).minusMonths(3) }),
    LESS_THAN_1_MONTH({ clock -> LocalDateTime.now(clock).minusMonths(1) }),
    LESS_THAN_1_WEEK({ clock -> LocalDateTime.now(clock).minusWeeks(1) }),
    ALL({ null }),
}