package com.emplk.realestatemanager.data.utils.type_converters

import androidx.room.TypeConverter
import com.emplk.realestatemanager.domain.amenity.Amenity

class AmenityTypeConverter {

    @TypeConverter
    fun fromAmenityList(amenities: List<Amenity>): String {
        return amenities.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toAmenityList(amenitiesString: String): List<Amenity> {
        return amenitiesString.split(",").map { Amenity.valueOf(it) }
    }
}