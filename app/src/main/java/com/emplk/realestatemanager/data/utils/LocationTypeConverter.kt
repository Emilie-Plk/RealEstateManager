package com.emplk.realestatemanager.data.utils

import androidx.room.TypeConverter
import com.emplk.realestatemanager.domain.add_property.entities.LocationEntity

class LocationTypeConverter {

    @TypeConverter
    fun fromLocation(location: LocationEntity): String {
        return "${location.latitude},${location.longitude}"
    }

    @TypeConverter
    fun toLocation(location: String): LocationEntity {
        val (latitude, longitude) = location.split(",")
        return LocationEntity(0, latitude.toDouble(), longitude.toDouble())
    }
}