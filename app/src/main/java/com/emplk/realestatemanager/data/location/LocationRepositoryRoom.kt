package com.emplk.realestatemanager.data.location

import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.location.LocationEntity
import com.emplk.realestatemanager.domain.location.LocationRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepositoryRoom @Inject constructor(
    private val locationDao: LocationDao,
    private val locationDtoEntityMapper: LocationDtoEntityMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : LocationRepository {

    override suspend fun add(locationEntity: LocationEntity) {
        withContext(coroutineDispatcherProvider.io) {
            val locationDtoEntity = locationDtoEntityMapper.mapToDtoEntity(locationEntity)
            locationDao.insert(locationDtoEntity)
        }
    }

    override suspend fun update(locationEntity: LocationEntity) {
        withContext(coroutineDispatcherProvider.io) {
            val locationDtoEntity = locationDtoEntityMapper.mapToDtoEntity(locationEntity)
            locationDao.update(locationDtoEntity)
        }
    }
}