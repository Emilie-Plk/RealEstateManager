package com.emplk.realestatemanager.data.amenity

import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.amenity.AmenityRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AmenityRepositoryRoom @Inject constructor(
    private val amenityDao: AmenityDao,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : AmenityRepository {
    override suspend fun addAmenity(amenityEntity: AmenityEntity) {
        withContext(coroutineDispatcherProvider.io) {
            amenityDao.insertAmenity(amenityEntity)
        }
    }

    override suspend fun updateAmenity(amenityEntity: AmenityEntity) {
        withContext(coroutineDispatcherProvider.io) {
            amenityDao.updateAmenity(amenityEntity)
        }
    }
}