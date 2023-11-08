package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.data.property.PropertyIdWithLatLong
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class FilterPropertiesByDistanceUseCase @Inject constructor() {
    fun invoke(
        propertyIdsWithLatLongs: List<PropertyIdWithLatLong>,
        locationLatLong: LatLng?,
        radiusInMiles: Int
    ): List<Long> =
        if (locationLatLong == null) {
            propertyIdsWithLatLongs.map { it.id }
        } else buildList {
            for (propertyIdWithLatLong in propertyIdsWithLatLongs) {
                val propertyLatLong = LatLng(propertyIdWithLatLong.latitude, propertyIdWithLatLong.longitude)
                if (haversineDistance(propertyLatLong, locationLatLong) <= radiusInMiles) {
                    add(propertyIdWithLatLong.id)
                }
            }
        }

    private fun haversineDistance(latLong1: LatLng, latLong2: LatLng): Double {
        val earthRadiusInMiles = 3958.8

        val lat1 = Math.toRadians(latLong1.latitude)
        val lon1 = Math.toRadians(latLong1.longitude)
        val lat2 = Math.toRadians(latLong2.latitude)
        val lon2 = Math.toRadians(latLong2.longitude)

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val a = (sin(dLat / 2) * sin(dLat / 2)
                + cos(lat1) * cos(lat2) * sin(dLon / 2) * sin(dLon / 2))
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadiusInMiles * c // Distance in miles
    }
}
