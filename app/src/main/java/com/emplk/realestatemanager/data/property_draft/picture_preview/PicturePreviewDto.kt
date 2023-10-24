package com.emplk.realestatemanager.data.property_draft.picture_preview

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "picture_previews",
    indices = [Index(value = ["id"], unique = false)]
)
data class PicturePreviewDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "property_draft_id", index = true)
    val propertyFormId: Long,
    val uri: String,
    val description: String?,
    @ColumnInfo(name = "is_featured")
    val isFeatured: Boolean,
)