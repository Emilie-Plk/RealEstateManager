package com.emplk.realestatemanager.data.property

import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.property.PropertyEntity
import com.emplk.realestatemanager.domain.property.PropertyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PropertyRepositoryRoom @Inject constructor(
    private val propertyDao: PropertyDao,
    private val propertyDtoEntityMapper: PropertyDtoEntityMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : PropertyRepository {

    override suspend fun add(propertyEntity: PropertyEntity): Long = withContext(coroutineDispatcherProvider.io) {
        val propertyDtoEntity = propertyDtoEntityMapper.mapToDtoEntity(propertyEntity)
        propertyDao.insert(propertyDtoEntity)
    }

    override fun getPropertiesAsFlow(): Flow<List<PropertyEntity>> = propertyDao
        .getPropertiesWithDetailsFlow()
        .map { propertyWithDetailsEntities ->
            propertyWithDetailsEntities.map { propertyWithDetailsEntity ->
                propertyDtoEntityMapper.mapToDomainEntity(
                    propertyWithDetailsEntity.property,
                    propertyWithDetailsEntity.location,
                    propertyWithDetailsEntity.pictures,
                    propertyWithDetailsEntity.amenities
                )
            }
        }
        .flowOn(coroutineDispatcherProvider.io)

    override fun getPropertyByIdAsFlow(propertyId: Long): Flow<PropertyEntity> = propertyDao
        .getPropertyById(propertyId)
        .map {
            propertyDtoEntityMapper.mapToDomainEntity(
                it.property,
                it.location,
                it.pictures,
                it.amenities
            )
        }
        .flowOn(coroutineDispatcherProvider.io)

    override suspend fun update(propertyEntity: PropertyEntity): Int =
        withContext(coroutineDispatcherProvider.io) {
            val propertyDtoEntity = propertyDtoEntityMapper.mapToDtoEntity(propertyEntity)
            propertyDao.update(propertyDtoEntity)
        }
}