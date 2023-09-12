package com.emplk.realestatemanager.data.property

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(
    tableName = "properties",
    indices = [Index(value = ["id"], unique = true)]
)
data class PropertyDtoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String,
    val price: BigDecimal,
    val surface: Int,
    val rooms: Int,
    val bedrooms: Int,
    val bathrooms: Int,
    val description: String,
    @ColumnInfo(name = "agent_name")
    val agentName: String,
    @ColumnInfo(name = "is_available_for_sale")
    val isAvailableForSale: Boolean,
    @ColumnInfo(name = "is_sold")
    val isSold: Boolean,
    @ColumnInfo(name = "entry_date")
    val entryDate: LocalDateTime,
    @ColumnInfo(name = "sale_date")
    val saleDate: LocalDateTime?,
)
