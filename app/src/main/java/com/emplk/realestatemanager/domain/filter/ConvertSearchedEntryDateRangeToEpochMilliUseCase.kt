package com.emplk.realestatemanager.domain.filter

import java.time.Clock
import java.time.ZoneOffset
import javax.inject.Inject

class ConvertSearchedEntryDateRangeToEpochMilliUseCase @Inject constructor(private val clock: Clock) {
    fun invoke(searchedEntryDateRange: SearchedEntryDateRange?): Long? {
        return if (searchedEntryDateRange == null || searchedEntryDateRange.searchedEntryDateLambda.invoke(clock) == null) null
        else return when (searchedEntryDateRange) {
            SearchedEntryDateRange.LESS_THAN_1_YEAR ->
                searchedEntryDateRange.searchedEntryDateLambda.invoke(clock)!!.atZone(ZoneOffset.UTC).toInstant()
                    .toEpochMilli()

            SearchedEntryDateRange.LESS_THAN_6_MONTHS ->
                searchedEntryDateRange.searchedEntryDateLambda.invoke(clock)!!.atZone(ZoneOffset.UTC).toInstant()
                    .toEpochMilli()

            SearchedEntryDateRange.LESS_THAN_3_MONTHS ->
                searchedEntryDateRange.searchedEntryDateLambda.invoke(clock)!!.atZone(ZoneOffset.UTC).toInstant()
                    .toEpochMilli()

            SearchedEntryDateRange.LESS_THAN_1_MONTH ->
                searchedEntryDateRange.searchedEntryDateLambda.invoke(clock)!!.atZone(ZoneOffset.UTC).toInstant()
                    .toEpochMilli()

            SearchedEntryDateRange.LESS_THAN_1_WEEK ->
                searchedEntryDateRange.searchedEntryDateLambda.invoke(clock)!!.atZone(ZoneOffset.UTC).toInstant()
                    .toEpochMilli()

            SearchedEntryDateRange.ALL -> null
        }
    }
}