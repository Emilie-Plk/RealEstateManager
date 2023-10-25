package com.emplk.realestatemanager.domain.map_picture

import javax.inject.Inject

class GenerateMapUrlWithApiKeyUseCase @Inject constructor() {
    companion object {
        private const val KEY = "key="
    }

    fun invoke(baseUrlWithParams: String): String {
        val completeUrl = baseUrlWithParams + KEY + "BLABLA"
        //BuildConfig.GOOGLE_API_KEY
        // Todo: activer la clé API pour le detail map bien fûr
        // Log.d("COUCOU", "invoke: $completeUrl")
        return completeUrl
    }

}