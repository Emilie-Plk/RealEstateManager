package com.emplk.realestatemanager.data.property

import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.property.PropertiesWithPicturesAndLocationEntity
import com.emplk.realestatemanager.domain.property.PropertyEntity
import com.emplk.realestatemanager.domain.property.PropertyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PropertyRepositoryRoom @Inject constructor(
    private val propertyDao: PropertyDao,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : PropertyRepository {

    override suspend fun add(propertyEntity: PropertyEntity): Long = withContext(coroutineDispatcherProvider.io) {
        propertyDao.insert(propertyEntity)
    }

    override fun getPropertiesAsFlow(): Flow<List<PropertiesWithPicturesAndLocationEntity>> = propertyDao
        .getPropertiesWithPicturesAndLocation()
        .flowOn(coroutineDispatcherProvider.io)

    override fun getPropertyByIdAsFlow(propertyId: Long): Flow<PropertiesWithPicturesAndLocationEntity> = propertyDao
        .getPropertyById(propertyId)
        .flowOn(coroutineDispatcherProvider.io)

    override suspend fun update(propertyEntity: PropertyEntity): Int = withContext(coroutineDispatcherProvider.io) {
        propertyDao.update(propertyEntity)
    }
}