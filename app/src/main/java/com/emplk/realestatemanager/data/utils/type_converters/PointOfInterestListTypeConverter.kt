package com.emplk.realestatemanager.data.utils.type_converters

import androidx.room.TypeConverter
import com.emplk.realestatemanager.domain.entities.PointOfInterestEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PointOfInterestListTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromList(list: List<PointOfInterestEntity>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(json: String): List<PointOfInterestEntity> {
        val type = object : TypeToken<List<PointOfInterestEntity>>() {}.type
        return gson.fromJson(json, type)
    }
}