package com.emplk.realestatemanager.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "city")
    val city: String,

    @ColumnInfo(name = "neighborhood")
    val neighborhood: String,

    @ColumnInfo(name = "postal_code")
    val postalCode: String,
)