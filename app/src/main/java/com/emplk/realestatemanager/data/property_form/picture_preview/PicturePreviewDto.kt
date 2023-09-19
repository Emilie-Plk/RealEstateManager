package com.emplk.realestatemanager.data.property_form.picture_preview

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "picture_previews")
data class PicturePreviewDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val uri: String,
    val description: String?,
    val isFeatured: Boolean,
)