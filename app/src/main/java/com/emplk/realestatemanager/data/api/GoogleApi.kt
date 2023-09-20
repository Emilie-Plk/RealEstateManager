package com.emplk.realestatemanager.data.api

import com.emplk.realestatemanager.data.autocomplete.response.AutocompleteResponse
import com.emplk.realestatemanager.data.geocoding.response.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleApi {

    @GET("place/autocomplete/json")
    suspend fun getAddressPredictions(
        @Query("input") input: String,
        @Query("type") type: String
    ): AutocompleteResponse

    @GET("/geocode/json")
    suspend fun getGeocode(
        @Query("place_id") placeId: String,
    ): GeocodingResponse
}