package com.emplk.realestatemanager.data.location

import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.location.LocationRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepositoryRoom @Inject constructor(
    private val locationDao: LocationDao,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : LocationRepository {

    override suspend fun add(location: LocationDtoEntity) {
        withContext(coroutineDispatcherProvider.io) {
            locationDao.insert(location)
        }
    }

    override suspend fun update(locationDtoEntity: LocationDtoEntity) {
        withContext(coroutineDispatcherProvider.io) {
            locationDao.update(locationDtoEntity)
        }
    }

}