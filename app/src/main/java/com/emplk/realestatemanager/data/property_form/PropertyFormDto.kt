package com.emplk.realestatemanager.data.property_form

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "property_forms")
data class PropertyFormDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String,
    val price: BigDecimal,
    val surface: String,
    val rooms: String,
    val bedrooms: String,
    val bathrooms: String,
    val description: String,
    val agentName: String,
)