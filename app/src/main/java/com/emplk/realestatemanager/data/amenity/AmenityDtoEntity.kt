package com.emplk.realestatemanager.data.amenity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "amenities",
)
data class AmenityDtoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "property_id")
    val propertyId: Long,
)