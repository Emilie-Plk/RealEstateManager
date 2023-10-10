package com.emplk.realestatemanager.domain.map_picture

import android.util.Log
import javax.inject.Inject

class GenerateMapUrlWithApiKeyUseCase @Inject constructor() {
    companion object {
        private const val KEY = "key="
    }

    fun invoke(baseUrlWithParams: String): String {
        val completeUrl = baseUrlWithParams + KEY + "COUCOU"
        //BuildConfig.GOOGLE_API_KEY
        Log.d("COUCOU", "invoke: $completeUrl")
        return completeUrl
    }

}