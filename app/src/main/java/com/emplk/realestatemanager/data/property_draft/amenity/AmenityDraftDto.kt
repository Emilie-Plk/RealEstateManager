package com.emplk.realestatemanager.data.property_draft.amenity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "amenities_draft")
data class AmenityDraftDto(
    @PrimaryKey
    val id: Long,
    val name: String,
    @ColumnInfo(name = "property_draft_id", index = true)
    val propertyFormId: Long,
)
