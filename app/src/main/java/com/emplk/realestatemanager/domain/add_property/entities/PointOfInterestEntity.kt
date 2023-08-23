package com.emplk.realestatemanager.domain.add_property.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "points_of_interest")
data class PointOfInterestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val isSelected: Boolean

    // probably many-to-many relationship with PropertyEntity with a Junction table
)