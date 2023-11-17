package com.emplk.realestatemanager.domain.filter

import java.time.Clock
import java.time.ZoneOffset
import javax.inject.Inject

class ConvertEntryDateStateToEpochMilliUseCase @Inject constructor(private val clock: Clock) {
    fun invoke(entryDateState: EntryDateState?): Long? {
        return if (entryDateState == null || entryDateState.entryDateLambda.invoke(clock) == null) null
        else return when (entryDateState) {
            EntryDateState.LESS_THAN_1_YEAR ->
                entryDateState.entryDateLambda.invoke(clock)!!.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()

            EntryDateState.LESS_THAN_6_MONTHS ->
                entryDateState.entryDateLambda.invoke(clock)!!.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()

            EntryDateState.LESS_THAN_3_MONTHS ->
                entryDateState.entryDateLambda.invoke(clock)!!.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()

            EntryDateState.LESS_THAN_1_MONTH ->
                entryDateState.entryDateLambda.invoke(clock)!!.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()

            EntryDateState.LESS_THAN_1_WEEK ->
                entryDateState.entryDateLambda.invoke(clock)!!.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()

            EntryDateState.ALL -> null
        }
    }
}