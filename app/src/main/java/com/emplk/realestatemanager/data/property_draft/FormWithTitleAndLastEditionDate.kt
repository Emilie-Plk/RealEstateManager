package com.emplk.realestatemanager.data.property_draft

import java.time.LocalDateTime

data class FormWithTitleAndLastEditionDate(
    val id: Long,
    val title: String,
    val lastEditionDate: LocalDateTime
)