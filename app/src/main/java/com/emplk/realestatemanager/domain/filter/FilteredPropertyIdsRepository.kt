package com.emplk.realestatemanager.domain.filter

interface FilteredPropertyIdsRepository {
    suspend fun getFilteredPropertyIds(): List<Long>
    fun setFilteredPropertyIds(ids: List<Long>)
    fun resetFilteredPropertyIds()
}