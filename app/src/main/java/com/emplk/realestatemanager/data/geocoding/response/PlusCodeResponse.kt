package com.emplk.realestatemanager.data.geocoding.response

import com.google.gson.annotations.SerializedName

data class PlusCodeResponse(
    @SerializedName("compound_code") val compoundCode: String?,
    @SerializedName("global_code") val globalCode: String?
)