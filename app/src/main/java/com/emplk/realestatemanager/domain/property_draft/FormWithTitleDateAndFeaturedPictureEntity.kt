package com.emplk.realestatemanager.domain.property_draft

import java.time.LocalDateTime

data class FormWithTitleDateAndFeaturedPictureEntity(
    val id: Long,
    val title: String?,   // TODO: voir pour virer les champs non null
    val lastEditionDate: LocalDateTime?,
    val featuredPicture: String?,
    val featuredPictureDescription: String?,
)