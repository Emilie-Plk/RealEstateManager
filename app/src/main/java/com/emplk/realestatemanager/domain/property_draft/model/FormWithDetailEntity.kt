package com.emplk.realestatemanager.domain.property_draft.model

import java.time.LocalDateTime

data class FormWithDetailEntity(
    val id: Long,
    val title: String?,
    val lastEditionDate: LocalDateTime?,
    val featuredPicture: String?,
    val featuredPictureDescription: String?,
)