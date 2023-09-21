package com.emplk.realestatemanager.data.property_form.location

import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.property_form.location.LocationFormEntity
import com.emplk.realestatemanager.domain.property_form.location.LocationFormRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationFormRepositoryRoom @Inject constructor(
    private val locationFormDao: LocationFormDao,
    private val locationFormMapper: LocationFormMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : LocationFormRepository {
    override suspend fun add(locationFormEntity: LocationFormEntity, propertyFormId: Long): Long =
        withContext(coroutineDispatcherProvider.io) {
            locationFormDao.insert(
                locationFormMapper.mapToLocationDto(locationFormEntity)
            )
        }

    override suspend fun update(locationFormEntity: LocationFormEntity, propertyFormId: Long): Int =
        withContext(coroutineDispatcherProvider.io) {
            locationFormDao.update(
                locationFormMapper.mapToLocationDto(locationFormEntity)
            )
        }

    override suspend fun delete(locationFormId: Long): Int =
        withContext(coroutineDispatcherProvider.io) {
            locationFormDao.delete(locationFormId)
        }
}