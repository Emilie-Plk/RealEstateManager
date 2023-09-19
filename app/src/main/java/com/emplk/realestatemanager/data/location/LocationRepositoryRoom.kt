package com.emplk.realestatemanager.data.location

import android.database.sqlite.SQLiteException
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.location.LocationEntity
import com.emplk.realestatemanager.domain.location.LocationRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepositoryRoom @Inject constructor(
    private val locationDao: LocationDao,
    private val locationMapper: LocationMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : LocationRepository {

    override suspend fun add(locationEntity: LocationEntity, propertyId: Long): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            try {
                val locationDtoEntity = locationMapper.mapToDtoEntity(locationEntity, propertyId)
                locationDao.insert(locationDtoEntity) == 1L
            } catch (e: SQLiteException) {
                e.printStackTrace()
                false
            }
        }

    override suspend fun update(locationEntity: LocationEntity, propertyId: Long): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            val locationDtoEntity = locationMapper.mapToDtoEntity(locationEntity, propertyId)
            locationDao.update(locationDtoEntity) == 1
        }
}