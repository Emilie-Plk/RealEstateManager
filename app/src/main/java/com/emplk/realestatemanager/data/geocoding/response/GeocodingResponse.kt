package com.emplk.realestatemanager.data.geocoding.response

import com.google.gson.annotations.SerializedName

data class GeocodingResponse (
    @SerializedName("results") val results: List<GeocodingResultResponse>?,
    @SerializedName("status") val status: String?
)