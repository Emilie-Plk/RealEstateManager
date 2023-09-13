package com.emplk.realestatemanager.domain.pictures

data class PictureEntity(
    val id: Long = 0,
    val propertyId: Long,
    val description: String,
    val uri: String,
    val isThumbnail: Boolean,
)
