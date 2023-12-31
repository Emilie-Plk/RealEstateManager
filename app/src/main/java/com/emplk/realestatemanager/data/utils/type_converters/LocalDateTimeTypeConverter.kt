package com.emplk.realestatemanager.data.utils.type_converters

import androidx.room.TypeConverter
import java.time.LocalDateTime

class LocalDateTimeTypeConverter {

    @TypeConverter
    fun toDate(dateString: String?): LocalDateTime? = if (dateString == null) {
        null
    } else {
        LocalDateTime.parse(dateString)
    }

    @TypeConverter
    fun toDateString(date: LocalDateTime?): String? = date?.toString()
}