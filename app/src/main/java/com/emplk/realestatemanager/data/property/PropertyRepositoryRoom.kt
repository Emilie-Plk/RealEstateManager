package com.emplk.realestatemanager.data.property

import com.emplk.realestatemanager.data.amenity.AmenityDao
import com.emplk.realestatemanager.data.amenity.AmenityDtoEntityMapper
import com.emplk.realestatemanager.data.location.LocationDao
import com.emplk.realestatemanager.data.location.LocationDtoEntityMapper
import com.emplk.realestatemanager.data.picture.PictureDao
import com.emplk.realestatemanager.data.picture.PictureDtoEntityMapper
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
    private val locationDao: LocationDao,
    private val pictureDao: PictureDao,
    private val amenityDao: AmenityDao,
    private val propertyDtoEntityMapper: PropertyDtoEntityMapper,
    private val locationDtoEntityMapper: LocationDtoEntityMapper,
    private val pictureDtoEntityMapper: PictureDtoEntityMapper,
    private val amenityDtoEntityMapper: AmenityDtoEntityMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : PropertyRepository {

    override suspend fun add(propertyEntity: PropertyEntity): Long = withContext(coroutineDispatcherProvider.io) {
        val propertyDtoEntity = propertyDtoEntityMapper.mapToDtoEntity(propertyEntity)
        val locationDtoEntity = locationDtoEntityMapper.mapToDtoEntity(propertyEntity.location)
        val pictureDtoEntities = pictureDtoEntityMapper.mapToDtoEntities(propertyEntity.pictures)
        val amenityDtoEntities = amenityDtoEntityMapper.mapToDtoEntities(propertyEntity.amenities)

         locationDao.insert(locationDtoEntity)

        pictureDtoEntities.map {
            pictureDao.insert(it)
        }

        amenityDtoEntities.map {
            amenityDao.insert(it)
        }

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