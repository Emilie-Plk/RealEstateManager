package com.emplk.realestatemanager.data.property_form

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "property_forms")
data class PropertyFormDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String? = null,
    val price: BigDecimal? = null,
    val surface: String? = null,
    val rooms: String? = null,
    val bedrooms: String? = null,
    val bathrooms: String? = null,
    val description: String? = null,
    val agentName: String? = null,
)