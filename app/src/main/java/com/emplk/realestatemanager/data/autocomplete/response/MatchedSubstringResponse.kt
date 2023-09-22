package com.emplk.realestatemanager.data.autocomplete.response

import com.google.gson.annotations.SerializedName

data class MatchedSubstringResponse(
    @SerializedName("length") val length: Int?,
    @SerializedName("offset") val offset: Int?
)