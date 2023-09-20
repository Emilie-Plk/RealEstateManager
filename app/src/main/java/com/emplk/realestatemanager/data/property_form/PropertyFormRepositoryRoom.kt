package com.emplk.realestatemanager.data.property_form

import com.emplk.realestatemanager.data.property_form.picture_preview.PropertyFormMapper
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.property_form.PropertyFormEntity
import com.emplk.realestatemanager.domain.property_form.PropertyFormRepository
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import javax.inject.Inject

class PropertyFormRepositoryRoom @Inject constructor(
    private val propertyFormDao: PropertyFormDao,
    private val propertyFormMapper: PropertyFormMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : PropertyFormRepository {
    override suspend fun add(propertyFormEntity: PropertyFormEntity): Long =
        withContext(coroutineDispatcherProvider.io) {
            propertyFormDao.insert(propertyFormMapper.mapToPropertyFormDto(propertyFormEntity))
        }

    override suspend fun updateType(pictureId: Long, newType: String) = withContext(coroutineDispatcherProvider.io) {
        propertyFormDao.updateType(pictureId, newType)
    }

    override suspend fun updatePrice(pictureId: Long, newPrice: BigDecimal) =
        withContext(coroutineDispatcherProvider.io) {
            propertyFormDao.updatePrice(pictureId, newPrice)
        }

    override suspend fun updateSurface(pictureId: Long, newSurface: Int) = withContext(coroutineDispatcherProvider.io) {
        propertyFormDao.updateSurface(pictureId, newSurface)
    }

    override suspend fun updateRooms(pictureId: Long, newRooms: Int) = withContext(coroutineDispatcherProvider.io) {
        propertyFormDao.updateRooms(pictureId, newRooms)
    }

    override suspend fun updateBedrooms(pictureId: Long, newBedrooms: Int) =
        withContext(coroutineDispatcherProvider.io) {
            propertyFormDao.updateBedrooms(pictureId, newBedrooms)
        }

    override suspend fun updateBathrooms(pictureId: Long, newBathrooms: Int) =
        withContext(coroutineDispatcherProvider.io) {
            propertyFormDao.updateBathrooms(pictureId, newBathrooms)
        }

    override suspend fun updateDescription(pictureId: Long, newDescription: String?) =
        withContext(coroutineDispatcherProvider.io) {
            propertyFormDao.updateDescription(pictureId, newDescription)
        }
}