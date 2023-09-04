package com.emplk.realestatemanager.domain.amenity

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "amenities",
)
data class AmenityEntity(
    val name: String,
    @ColumnInfo(name = "property_id")
    val propertyId: Long,
)