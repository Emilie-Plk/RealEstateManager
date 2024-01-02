package com.emplk.realestatemanager.data.filter

import com.emplk.realestatemanager.domain.filter.PropertiesFilterRepository
import com.emplk.realestatemanager.domain.filter.model.PropertiesFilterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class PropertiesFilterRepositoryImpl @Inject constructor() : PropertiesFilterRepository {
    private val propertiesIdsMutableStateFlow: MutableStateFlow<PropertiesFilterEntity?> = MutableStateFlow(null)

    override fun setPropertiesFilter(propertiesFilterEntity: PropertiesFilterEntity) {
        propertiesIdsMutableStateFlow.tryEmit(propertiesFilterEntity)
    }

    override fun getPropertiesFilter(): Flow<PropertiesFilterEntity?> = propertiesIdsMutableStateFlow

    override fun resetPropertiesFilter() {
        propertiesIdsMutableStateFlow.tryEmit(null)
    }
}