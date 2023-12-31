package com.emplk.realestatemanager.data.autocomplete.response

import com.google.gson.annotations.SerializedName

data class AutocompleteResponse(
    @SerializedName("predictions") val predictions: List<PredictionResponse>?,
    @SerializedName("status") val status: String?
)
