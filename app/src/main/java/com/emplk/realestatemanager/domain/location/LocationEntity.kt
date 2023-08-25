package com.emplk.realestatemanager.domain.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "property_id")
    val propertyId: Long,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val city: String,
    val neighborhood: String,
    @ColumnInfo(name = "postal_code")
    val postalCode: String,
)