package com.emplk.realestatemanager.data.api

import com.emplk.realestatemanager.data.currency_rate.response.FixerCurrencyRateResponse
import retrofit2.http.GET

interface FixerApi {
    @GET("latest")
    suspend fun getCurrencyRates(): FixerCurrencyRateResponse
}