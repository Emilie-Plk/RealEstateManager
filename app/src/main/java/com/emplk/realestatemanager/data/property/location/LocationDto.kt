package com.emplk.realestatemanager.data.property.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "property_id", index = true)
    val propertyId: Long,
    val address: String,
    @ColumnInfo(name = "miniature_map_path")
    val miniatureMapPath: String,
    val latitude: String,
    val longitude: String,
)