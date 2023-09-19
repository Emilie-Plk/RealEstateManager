package com.emplk.realestatemanager.domain.property_form.picture_preview

data class PicturePreviewEntity(
    val id: Long = 0,
    val propertyFormId : Long,
    val uri: String,
    val description: String?,
    val isFeatured: Boolean,
)
