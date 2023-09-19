package com.emplk.realestatemanager.data.amenity

import android.database.sqlite.SQLiteException
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.amenity.AmenityRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AmenityRepositoryRoom @Inject constructor(
    private val amenityDao: AmenityDao,
    private val amenityMapper: AmenityMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : AmenityRepository {
    override suspend fun addAmenity(amenityEntity: AmenityEntity, propertyId: Long): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            try {
                val amenityDtoEntity = amenityMapper.mapToDtoEntity(amenityEntity, propertyId)
                amenityDao.insert(amenityDtoEntity) == 1L
            } catch (e: SQLiteException) {
                e.printStackTrace()
                false
            }
        }

    override suspend fun updateAmenity(amenityEntity: AmenityEntity, propertyId: Long): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            val amenityDtoEntity = amenityMapper.mapToDtoEntity(amenityEntity, propertyId)
            amenityDao.update(amenityDtoEntity) == 1
        }
}