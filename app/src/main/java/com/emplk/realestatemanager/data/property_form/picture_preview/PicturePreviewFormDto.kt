package com.emplk.realestatemanager.data.property_form.picture_preview

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "picture_previews")
data class PicturePreviewFormDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "property_form_id", index = true)
    val propertyFormId: Long,
    val uri: String,
    val description: String?,
    @ColumnInfo(name = "is_featured")
    val isFeatured: Boolean,
)