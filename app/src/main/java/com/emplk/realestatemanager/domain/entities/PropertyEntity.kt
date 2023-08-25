package com.emplk.realestatemanager.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "properties")
data class PropertyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "price")
    val price: Int,

    @ColumnInfo(name = "surface")
    val surface: Int,

    @ColumnInfo(name = "rooms")
    val rooms: Int,

    @ColumnInfo(name = "description")
    val description: String,

    // TODO Utiliser embedded et relations + transaction
    @ColumnInfo(name = "photos")
    val photos: List<PropertyPictureEntity>,

    // TODO Utiliser  Utiliser embedded et relations + transaction
    @ColumnInfo(name = "location")
    val location: LocationEntity,

    // TODO: Stocker plutôt une Enum pour Room et "mapper" côté business/app
    @ColumnInfo(name = "points_of_interest")
    val pointsOfInterest: List<PointOfInterestEntity>,

    @ColumnInfo(name = "agent_name")
    val agent: String,

    @ColumnInfo(name = "is_available_for_sale")
    val isAvailableForSale: Boolean,

    @ColumnInfo(name = "is_sold")
    val isSold: Boolean,

    @ColumnInfo(name = "entry_date")
    val entryDate: LocalDateTime,

    @ColumnInfo(name = "sale_date")
    val saleDate: LocalDateTime?,
)
