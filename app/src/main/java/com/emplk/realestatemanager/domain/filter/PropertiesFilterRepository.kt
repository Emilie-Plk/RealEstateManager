package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.domain.filter.model.PropertiesFilterEntity
import kotlinx.coroutines.flow.Flow

interface PropertiesFilterRepository {
    fun setPropertiesFilter(propertiesFilterEntity: PropertiesFilterEntity)
    fun getPropertiesFilter(): Flow<PropertiesFilterEntity?>
    fun resetPropertiesFilter()
}