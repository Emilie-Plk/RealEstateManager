package com.emplk.realestatemanager.domain.property_draft

import java.time.LocalDateTime

data class FormWithTitleDateAndFeaturedPictureEntity(
    val id: Long,
    val title: String?,
    val lastEditionDate: LocalDateTime?,
    val featuredPicture: String?,
    val featuredPictureDescription: String?,
)