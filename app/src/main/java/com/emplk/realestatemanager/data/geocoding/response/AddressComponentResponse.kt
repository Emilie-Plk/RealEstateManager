package com.emplk.realestatemanager.data.geocoding.response

import com.google.gson.annotations.SerializedName

data class AddressComponentResponse (
    @SerializedName("long_name") val longName: String?,
    @SerializedName("short_name") val shortName: String?,
    @SerializedName("types") val types: List<String>?
)