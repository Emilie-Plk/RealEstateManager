package com.emplk.realestatemanager.data.property_draft

import androidx.room.ColumnInfo
import java.time.LocalDateTime

data class FormWithTitleAndLastEditionDateEntity(
    val id: Long,
    val title: String?,
    @ColumnInfo(name = "last_edition_date")
    val lastEditionDate: LocalDateTime?,
)