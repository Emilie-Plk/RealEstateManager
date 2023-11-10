package com.emplk.realestatemanager.data.utils.type_converters

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalTypeConverter {

    @TypeConverter
    fun bigDecimalToDouble(bigDecimal: BigDecimal?): Double {
        return bigDecimal?.toDouble() ?: 0.0
    }

    @TypeConverter
    fun doubleToBigDecimal(value: Double?): BigDecimal {
        return value?.toBigDecimal() ?: BigDecimal.ZERO
    }
}