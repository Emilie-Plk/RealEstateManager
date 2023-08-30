package com.emplk.realestatemanager.ui.detail

import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText

data class DetailViewState(
    val id: Long,
    val type: String,
    val featuredPicture: NativePhoto,
    val pictures: List<String>,
    val price: NativeText,
    val surface: NativeText,
    val rooms: NativeText,
    val bathrooms: NativeText,
    val bedrooms: NativeText,
    val description: String,
    val address: NativeText,
    val amenitySchool: Boolean,
    val amenityPark: Boolean,
    val amenityShoppingMall: Boolean,
    val amenityRestaurant: Boolean,
    val amenityConcierge: Boolean,
    val amenityPublicTransportation: Boolean,
    val amenityHospital: Boolean,
    val amenityLibrary: Boolean,
    val entryDate: NativeText,
    val agentName: NativeText,
    val isSold: Boolean,
    val saleDate: NativeText?,
)