package com.emplk.realestatemanager.data.property

import android.util.Log
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.PropertyRepository
import com.emplk.realestatemanager.domain.add_property.entities.PropertyEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PropertyRepositoryRoom @Inject constructor(
    private val propertyDao: PropertyDao,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : PropertyRepository {

    override suspend fun add(propertyEntity: PropertyEntity) = withContext(coroutineDispatcherProvider.io) {
        Log.d("COUCOU", "add: " + propertyEntity.toString())
            propertyDao.insert(propertyEntity)
    }

    override suspend fun update(propertyEntity: PropertyEntity) = withContext(coroutineDispatcherProvider.io) {
        propertyDao.update(propertyEntity)
    }

    override fun getPropertiesAsFlow(): Flow<List<PropertyEntity>> = propertyDao
        .getProperties()
        .flowOn(coroutineDispatcherProvider.io)

    override fun getPropertyByIdAsFlow(propertyId: Long): Flow<PropertyEntity> = propertyDao
        .getPropertyById(propertyId)
        .flowOn(coroutineDispatcherProvider.io)
}