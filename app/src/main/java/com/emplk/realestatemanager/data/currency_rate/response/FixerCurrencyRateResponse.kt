package com.emplk.realestatemanager.data.currency_rate.response

import com.google.gson.annotations.SerializedName

data class FixerCurrencyRateResponse(

    @SerializedName("date")
    val date: String?,

    @SerializedName("success")
    val success: Boolean?,

    @SerializedName("rates")
    val rateResponse: RateResponse?,

    @SerializedName("timestamp")
    val timestamp: Int?,

    @SerializedName("base")
    val base: String?,
)