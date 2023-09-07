package com.emplk.realestatemanager.data.picture

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pictures")
data class PictureDtoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "property_id")
    val propertyId: Long,
    val uri: String,
    val description: String,
    @ColumnInfo(name = "is_thumbnail")
    val isThumbnail: Boolean
)