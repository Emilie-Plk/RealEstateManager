package com.emplk.realestatemanager.data.property_draft

import androidx.room.ColumnInfo
import java.time.LocalDateTime

data class FormWithTitleAndLastEditionDate(
    val id: Long,
    val title: String?,
    @ColumnInfo(name = "last_edition_date")
    val lastEditionDate: LocalDateTime?,
    @ColumnInfo(name = "entry_date")
    val entryDate: LocalDateTime?,
)