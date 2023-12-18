package com.emplk.realestatemanager.data.property_type

import androidx.annotation.StringRes
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.property_type.PropertyTypeRepository
import javax.inject.Inject

class PropertyTypeRepositoryImpl @Inject constructor() : PropertyTypeRepository {
    private val propertyTypes: List<PropertyType> = listOf(
        PropertyType.HOUSE,
        PropertyType.FLAT,
        PropertyType.DUPLEX,
        PropertyType.PENTHOUSE,
        PropertyType.VILLA,
        PropertyType.MANOR,
        PropertyType.ALL,
        PropertyType.OTHER,
    )

    override fun getPropertyTypes(): List<PropertyType> = propertyTypes
}

enum class PropertyType(
    val id: Long,
    val databaseName: String,
    @StringRes val stringRes: Int
) {
    HOUSE(1L, "House", R.string.type_house),
    FLAT(2L, "Flat", R.string.type_flat),
    DUPLEX(3L, "Duplex", R.string.type_duplex),
    PENTHOUSE(4L, "Penthouse", R.string.type_penthouse),
    VILLA(5L, "Villa", R.string.type_villa),
    MANOR(6L, "Manor", R.string.type_manor),
    OTHER(7L, "Other", R.string.type_other),
    ALL(0L, "All", R.string.type_all),
}