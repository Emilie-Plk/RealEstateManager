package com.emplk.realestatemanager.ui.detail

import com.emplk.realestatemanager.ui.detail.picture_banner.PictureBannerViewState
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText

sealed class DetailViewState(val type: Type) {

    enum class Type {
        PROPERTY_DETAIL,
        LOADING,
    }

    data class PropertyDetail(
        val id: Long,
        val propertyType: String,
        val pictures: List<PictureBannerViewState>,
        val mapMiniature: NativePhoto,
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
        val amenityGym: Boolean,
        val entryDate: NativeText,
        val agentName: NativeText,
        val isSold: Boolean,
        val saleDate: NativeText?,
    ) : DetailViewState(Type.PROPERTY_DETAIL)

    object LoadingState : DetailViewState(Type.LOADING)
}