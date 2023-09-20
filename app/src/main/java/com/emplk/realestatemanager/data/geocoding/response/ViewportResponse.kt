package com.emplk.realestatemanager.data.geocoding.response

import com.google.gson.annotations.SerializedName

data class ViewportResponse(
    @SerializedName("northeast") val northeast: LocationResponse?,
    @SerializedName("southwest") val southwest: LocationResponse?
)