package com.emplk.realestatemanager.data.amenity

import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.amenity.AmenityRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AmenityRepositoryRoom @Inject constructor(
    private val amenityDao: AmenityDao,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : AmenityRepository {
    override suspend fun addAmenity(amenityDtoEntity: AmenityDtoEntity) {
        withContext(coroutineDispatcherProvider.io) {
            amenityDao.insert(amenityDtoEntity)
        }
    }

    override suspend fun updateAmenity(amenityDtoEntity: AmenityDtoEntity) {
        withContext(coroutineDispatcherProvider.io) {
            amenityDao.update(amenityDtoEntity)
        }
    }
}