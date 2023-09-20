package com.emplk.realestatemanager.data.geocoding.response

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("lat") val lat: String?,
    @SerializedName("lng") val lng: String?
)