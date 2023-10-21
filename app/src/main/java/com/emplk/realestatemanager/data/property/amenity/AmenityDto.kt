package com.emplk.realestatemanager.data.property.amenity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "amenities",
    indices = [Index(value = ["id"], unique = true)])
data class AmenityDto(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "property_id", index = true)
    val propertyId: Long,
)