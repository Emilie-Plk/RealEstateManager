package com.emplk.realestatemanager.data.property.location

import android.database.sqlite.SQLiteException
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.property.location.LocationEntity
import com.emplk.realestatemanager.domain.property.location.LocationRepository
import com.emplk.realestatemanager.domain.property.location.PropertyLatLongEntity
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
                val locationDtoEntity = locationMapper.mapToDto(locationEntity, propertyId)
                locationDao.insert(locationDtoEntity) == 1L
            } catch (e: SQLiteException) {
                e.printStackTrace()
                false
            }
        }

    override suspend fun getAllPropertyLatLong(): List<PropertyLatLongEntity> =
        withContext(coroutineDispatcherProvider.io) {
            try {
                locationDao.getAllPropertyLatLong().mapNotNull { propertyLatLongDto ->
                    locationMapper.mapToPropertyLatLongEntity(propertyLatLongDto)
                }
            } catch (e: SQLiteException) {
                e.printStackTrace()
                emptyList()
            }
        }
}