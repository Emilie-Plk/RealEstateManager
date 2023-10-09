package com.emplk.realestatemanager.data.api

import com.emplk.realestatemanager.data.currency_rate.response.FixerCurrencyRateResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FixerApi {
    @GET("latest")
    suspend fun getLatestCurrencyRates(): FixerCurrencyRateResponse
}