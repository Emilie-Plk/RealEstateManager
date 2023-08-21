package com.emplk.realestatemanager.domain.add_property

data class PropertyEntity(
    val id: Long,
    val type: String,
    val price: Int,
    val surface: Int,
    val rooms: Int,
    val description: String,
    val address: String,
    val latitude: LocationEntity,
    val pointsOfInterest: List<PointOfInterest>,
    val isAvailableForSale: Boolean,
    val entryDate: String,
    val saleDate: String,
    val agent: String,
    val photos: List<String>,
)
