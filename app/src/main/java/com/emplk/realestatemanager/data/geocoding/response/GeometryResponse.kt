package com.emplk.realestatemanager.data.geocoding.response

import com.google.gson.annotations.SerializedName

data class GeometryResponse(
    @SerializedName("location") val location: LocationResponse?,
    @SerializedName("location_type") val locationType: String?,
    @SerializedName("viewport") val viewport: ViewportResponse?
)