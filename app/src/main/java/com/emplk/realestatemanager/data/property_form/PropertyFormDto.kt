package com.emplk.realestatemanager.data.property_form

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "property_forms")
data class PropertyFormDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String?,
    val price: BigDecimal? = BigDecimal.ZERO,
    val surface: Int?,
    val rooms: Int?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val description: String?,
    val agentName: String?,
)