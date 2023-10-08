package com.emplk.realestatemanager.data.utils.type_converters

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalTypeConverter {

    @TypeConverter
    fun fromBigDecimal(bigDecimal: BigDecimal): String {
        return bigDecimal.toString()
    }

    @TypeConverter
    fun toBigDecimal(bigDecimalString: String): BigDecimal {
        return BigDecimal(bigDecimalString)
    }
}