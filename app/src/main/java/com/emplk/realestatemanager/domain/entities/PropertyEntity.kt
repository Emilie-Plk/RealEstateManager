package com.emplk.realestatemanager.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "properties",
)
data class PropertyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String,
    val price: Int,
    val surface: Int,
    val rooms: Int,
    val description: String,
    @ColumnInfo(name = "amenities")
    val amenities: List<Amenity>,
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
