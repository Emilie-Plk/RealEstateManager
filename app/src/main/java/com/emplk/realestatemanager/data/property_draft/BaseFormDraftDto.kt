package com.emplk.realestatemanager.data.property_draft

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "property_drafts")
data class BaseFormDraftDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String?,
    val price: BigDecimal,
    val surface: String?,
    val address: String?,
    @ColumnInfo(name = "is_address_valid")
    val isAddressValid: Boolean,
    val rooms: Int?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val description: String?,
    val agentName: String?,
)