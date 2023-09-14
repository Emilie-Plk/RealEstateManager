package com.emplk.realestatemanager.data.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationDtoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "property_id", index = true)
    val propertyId: Long,
    val address: String,
    val city: String,
    @ColumnInfo(name = "postal_code")
    val postalCode: String,
    val latitude: Double,
    val longitude: Double,
)