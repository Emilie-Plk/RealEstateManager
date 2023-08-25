package com.emplk.realestatemanager.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pictures")
data class PropertyPictureEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "property_id")
    val propertyId: Long,

    @ColumnInfo(name = "uri")
    val uri: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "is_thumbnail")
    val isThumbnail: Boolean
)
