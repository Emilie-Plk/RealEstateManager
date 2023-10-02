package com.emplk.realestatemanager.data.property.location

import androidx.room.ColumnInfo

data class PropertyLatLongDto(
    @ColumnInfo(name = "property_id")
    val propertyId: Long,
    val latitude: Double?,
    val longitude: Double?,
)
