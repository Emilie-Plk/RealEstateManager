package com.emplk.realestatemanager.data.property_draft

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(tableName = "base_form_drafts", indices = [Index(value = ["id"], unique = false)])
data class FormDraftDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String?,
    val type: String?,
    val price: BigDecimal,
    val surface: BigDecimal,
    val address: String?,
    @ColumnInfo(name = "is_address_valid")
    val isAddressValid: Boolean,
    val rooms: Int?,
    val bedrooms: Int?,
    val bathrooms: Int?,
    val description: String?,
    @ColumnInfo(name = "amenity_school")
    val amenitySchool: Boolean,
    @ColumnInfo(name = "amenity_park")
    val amenityPark: Boolean,
    @ColumnInfo(name = "amenity_shopping")
    val amenityShopping: Boolean,
    @ColumnInfo(name = "amenity_restaurant")
    val amenityRestaurant: Boolean,
    @ColumnInfo(name = "amenity_concierge")
    val amenityConcierge: Boolean,
    @ColumnInfo(name = "amenity_gym")
    val amenityGym: Boolean,
    @ColumnInfo(name = "amenity_transportation")
    val amenityTransportation: Boolean,
    @ColumnInfo(name = "amenity_hospital")
    val amenityHospital: Boolean,
    @ColumnInfo(name = "amenity_library")
    val amenityLibrary: Boolean,
    @ColumnInfo(name = "agent_name")
    val agentName: String?,
    @ColumnInfo(name = "is_sold")
    val isSold: Boolean,
    @ColumnInfo(name = "entry_date")
    val entryDate: LocalDateTime?,
    @ColumnInfo(name = "sale_date")
    val saleDate: LocalDateTime?,
    @ColumnInfo(name = "last_edition_date")
    val lastEditionDate: LocalDateTime?,
)