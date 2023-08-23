package com.emplk.realestatemanager.data.utils

import androidx.room.TypeConverter
import com.emplk.realestatemanager.domain.add_property.entities.PropertyPicture
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PropertyPictureListTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromList(list: List<PropertyPicture>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(json: String): List<PropertyPicture> {
        val type = object : TypeToken<List<PropertyPicture>>() {}.type
        return gson.fromJson(json, type)
    }
}
