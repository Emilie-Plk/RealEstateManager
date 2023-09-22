package com.emplk.realestatemanager.domain.property_form.picture_preview

data class PicturePreviewEntity(
    val id: Long = 0,
    val propertyFormId: Long = 0,
    val uri: String? = null,
    val description: String? = null,
    val isFeatured: Boolean?,
)
