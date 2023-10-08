package com.emplk.realestatemanager.data.property_draft

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "property_drafts")
data class PropertyDraftDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String?,
    val price: BigDecimal,
    val surface: String?,
    val address: String?,
    val rooms: Int?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val description: String?,
    val agentName: String?,
)