package com.emplk.realestatemanager.data.api

import com.emplk.realestatemanager.data.autocomplete.response.AutocompleteResponse
import com.emplk.realestatemanager.data.geocoding.response.GeocodingResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming

interface GoogleApi {

    @GET("place/autocomplete/json")
    suspend fun getAddressPredictions(
        @Query("input") input: String,
        @Query("type") type: String
    ): AutocompleteResponse

    @GET("geocode/json")
    suspend fun getGeocode(
        @Query("address") address: String,
    ): GeocodingResponse

    @GET("staticmap")
    @Streaming
    suspend fun getMap(
        @Query("center") center: String,
        @Query("zoom") zoom: Int,
        @Query("size") size: String,
        @Query("markers") markers: String,
    ): Response<ResponseBody>
}