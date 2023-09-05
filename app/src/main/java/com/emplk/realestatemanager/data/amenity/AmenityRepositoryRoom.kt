package com.emplk.realestatemanager.data.amenity

import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.amenity.AmenityRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AmenityRepositoryRoom @Inject constructor(
    private val amenityDao: AmenityDao,
    private val amenityDtoEntityMapper: AmenityDtoEntityMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : AmenityRepository {
    override suspend fun addAmenity(amenityEntity: AmenityEntity) {
        withContext(coroutineDispatcherProvider.io) {
            val amenityDtoEntity = amenityDtoEntityMapper.mapToDtoEntity(amenityEntity)
            amenityDao.insert(amenityDtoEntity)
        }
    }

    override suspend fun updateAmenity(amenityEntity: AmenityEntity) {
        withContext(coroutineDispatcherProvider.io) {
            val amenityDtoEntity = amenityDtoEntityMapper.mapToDtoEntity(amenityEntity)
            amenityDao.update(amenityDtoEntity)
        }
    }
}