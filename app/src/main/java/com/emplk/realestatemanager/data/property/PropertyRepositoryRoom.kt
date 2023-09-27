package com.emplk.realestatemanager.data.property

import android.database.sqlite.SQLiteException
import com.emplk.realestatemanager.data.property.amenity.AmenityDao
import com.emplk.realestatemanager.data.property.amenity.AmenityMapper
import com.emplk.realestatemanager.data.property.location.LocationDao
import com.emplk.realestatemanager.data.property.location.LocationMapper
import com.emplk.realestatemanager.data.property.picture.PictureDao
import com.emplk.realestatemanager.data.property.picture.PictureMapper
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.property.PropertyEntity
import com.emplk.realestatemanager.domain.property.PropertyRepository
import kotlinx.coroutines.async
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
    private val propertyMapper: PropertyMapper,
    private val locationMapper: LocationMapper,
    private val pictureMapper: PictureMapper,
    private val amenityMapper: AmenityMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : PropertyRepository {

    override suspend fun add(propertyEntity: PropertyEntity): Long? = withContext(coroutineDispatcherProvider.io) {
        try {
            propertyDao.insert(propertyMapper.mapToDtoEntity(propertyEntity))
        } catch (e: SQLiteException) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun addPropertyWithDetails(propertyEntity: PropertyEntity): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            val propertyId = add(propertyEntity) ?: return@withContext false

            val locationAsync = async {
                val locationDtoEntity = locationMapper.mapToDtoEntity(propertyEntity.location, propertyId)
                locationDao.insert(locationDtoEntity)
            }

            val picturesAsync = propertyEntity.pictures.map { pictureEntity ->
                async {
                    val pictureDtoEntity = pictureMapper.mapToDtoEntity(pictureEntity, propertyId)
                    pictureDao.insert(pictureDtoEntity)
                }
            }

            val amenitiesAsync = propertyEntity.amenities.map {
                async {
                    val amenityDtoEntity = amenityMapper.mapToDtoEntity(it, propertyId)
                    amenityDao.insert(amenityDtoEntity)
                }
            }

            // Wait for all child jobs to complete
            (listOf(locationAsync) + picturesAsync + amenitiesAsync).all { it.await() != null }
        }


    override fun getPropertiesAsFlow(): Flow<List<PropertyEntity>> = propertyDao
        .getPropertiesWithDetailsAsFlow()
        .map { propertyWithDetailsEntities ->
            propertyWithDetailsEntities.map { propertyWithDetailsEntity ->
                propertyMapper.mapToDomainEntity(
                    propertyWithDetailsEntity.property,
                    propertyWithDetailsEntity.location,
                    propertyWithDetailsEntity.pictures,
                    propertyWithDetailsEntity.amenities
                )
            }
        }
        .flowOn(coroutineDispatcherProvider.io)

    override fun getPropertyByIdAsFlow(propertyId: Long): Flow<PropertyEntity> = propertyDao
        .getPropertyByIdAsFlow(propertyId)
        .map {
            propertyMapper.mapToDomainEntity(
                it.property,
                it.location,
                it.pictures,
                it.amenities
            )
        }
        .flowOn(coroutineDispatcherProvider.io)

    override suspend fun update(propertyEntity: PropertyEntity): Int =
        withContext(coroutineDispatcherProvider.io) {
            val propertyDtoEntity = propertyMapper.mapToDtoEntity(propertyEntity)
            propertyDao.update(propertyDtoEntity)
        }
}