package com.emplk.realestatemanager.data.property_form.amenity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "amenity_forms")
data class AmenityFormDto(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "property_form_id", index = true)
    val propertyFormId: Long,
    val name: String,
)
