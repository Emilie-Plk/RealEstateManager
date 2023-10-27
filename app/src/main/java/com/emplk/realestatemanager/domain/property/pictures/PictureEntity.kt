package com.emplk.realestatemanager.domain.property.pictures

data class PictureEntity(
    val id: Long,
    val uri: String,
    val description: String?,
    val isFeatured: Boolean,
)
