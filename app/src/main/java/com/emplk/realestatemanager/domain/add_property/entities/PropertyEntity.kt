package com.emplk.realestatemanager.domain.add_property.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "properties")
data class PropertyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val type: String,
    val price: Int,
    val surface: Int,
    val rooms: Int,
    val description: String,
    val photos: List<PropertyPicture>,
    val address: String,
    val location: LocationEntity,
    val pointsOfInterest: List<PointOfInterestEntity>,
    val isAvailableForSale: Boolean,
    val entryDate: LocalDateTime,
    val saleDate: LocalDateTime?,
    val agent: String,
)
