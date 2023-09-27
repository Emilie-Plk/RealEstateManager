package com.emplk.realestatemanager.data.property.amenity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "amenities",
)
data class AmenityDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "property_id", index = true)
    val propertyId: Long,
)