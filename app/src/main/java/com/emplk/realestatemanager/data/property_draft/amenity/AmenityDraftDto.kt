package com.emplk.realestatemanager.data.property_draft.amenity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "amenities_draft",
    indices = [Index(value = ["id"], unique = false)])
data class AmenityDraftDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "property_draft_id", index = true)
    val propertyFormId: Long,
)
