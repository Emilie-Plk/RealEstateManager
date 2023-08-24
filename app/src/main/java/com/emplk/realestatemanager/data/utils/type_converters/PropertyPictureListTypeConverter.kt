package com.emplk.realestatemanager.data.utils.type_converters

import androidx.room.TypeConverter
import com.emplk.realestatemanager.domain.entities.PropertyPictureEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class PropertyPictureListTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromList(list: List<PropertyPictureEntity>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(json: String): List<PropertyPictureEntity> {
        val type = object : TypeToken<List<PropertyPictureEntity>>() {}.type
        return gson.fromJson(json, type)
    }
}
