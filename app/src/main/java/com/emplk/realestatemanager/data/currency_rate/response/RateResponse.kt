package com.emplk.realestatemanager.data.currency_rate.response

import com.google.gson.annotations.SerializedName

data class RateResponse(
    @SerializedName("USD")
    val usd: String?
)