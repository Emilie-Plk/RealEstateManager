package com.emplk.realestatemanager.data.property_form.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "location_forms"
)
data class LocationFormDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "property_form_id", index = true)
    val propertyFormId: Long,
    val address: String?,
    val placeId: String?,
    val latitude: String?,
    val longitude: String?,
)