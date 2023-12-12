package com.emplk.realestatemanager.data.property.location

import androidx.room.ColumnInfo
import java.time.LocalDateTime

data class PropertyWithLatLongAndSaleDateDto(
    @ColumnInfo(name = "property_id")
    val propertyId: Long,
    val latitude: Double?,
    val longitude: Double?,
    @ColumnInfo(name = "sale_date")
    val saleDate: LocalDateTime?,
)
