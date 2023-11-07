package com.emplk.realestatemanager.data

import com.emplk.realestatemanager.domain.filter.FilteredPropertyIdsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class FilteredPropertyIdsRepositoryImpl @Inject constructor() : FilteredPropertyIdsRepository {
    private val propertiesIdsMutableStateFlow = MutableStateFlow<List<Long>>(emptyList())

    override suspend fun getFilteredPropertyIds(): List<Long> = propertiesIdsMutableStateFlow.value

    override fun setFilteredPropertyIds(ids: List<Long>) {
        propertiesIdsMutableStateFlow.tryEmit(ids)
    }

    override fun resetFilteredPropertyIds() {
        propertiesIdsMutableStateFlow.tryEmit(emptyList())
    }
}