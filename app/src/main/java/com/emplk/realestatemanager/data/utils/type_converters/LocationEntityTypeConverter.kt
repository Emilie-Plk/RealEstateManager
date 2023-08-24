package com.emplk.realestatemanager.data.utils.type_converters

import androidx.room.TypeConverter
import com.emplk.realestatemanager.domain.entities.LocationEntity
import com.google.gson.Gson

class LocationEntityTypeConverter {

    @TypeConverter
    fun fromLocationEntityToString(locationEntity: LocationEntity): String {
        return Gson().toJson(locationEntity)
    }

    @TypeConverter
    fun fromStringToLocationEntity(string: String): LocationEntity {
        return Gson().fromJson(string, LocationEntity::class.java)
    }
}