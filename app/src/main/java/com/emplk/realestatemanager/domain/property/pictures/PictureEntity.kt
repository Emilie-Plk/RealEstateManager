package com.emplk.realestatemanager.domain.property.pictures

data class PictureEntity(
    val id: Long,
    val description: String?,
    val uri: String,
    val isFeatured: Boolean,
)
