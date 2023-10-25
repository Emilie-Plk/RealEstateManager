package com.emplk.realestatemanager.domain.property_draft

import java.time.LocalDateTime

data class FormTitleAndDate(
    val id: Long,
    val title: String,
    val lastEditionDate: LocalDateTime
)
