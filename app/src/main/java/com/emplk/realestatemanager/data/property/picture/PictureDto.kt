package com.emplk.realestatemanager.data.property.picture

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pictures")
data class PictureDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "property_id", index = true)
    val propertyId: Long,
    val uri: String,
    val description: String,
    @ColumnInfo(name = "is_featured")
    val isFeatured: Boolean
)
