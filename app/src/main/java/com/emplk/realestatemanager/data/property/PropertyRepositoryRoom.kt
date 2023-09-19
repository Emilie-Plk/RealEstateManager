package com.emplk.realestatemanager.data.property

import android.database.sqlite.SQLiteException
import com.emplk.realestatemanager.data.amenity.AmenityDao
import com.emplk.realestatemanager.data.amenity.AmenityMapper
import com.emplk.realestatemanager.data.location.LocationDao
import com.emplk.realestatemanager.data.location.LocationMapper
import com.emplk.realestatemanager.data.picture.PictureDao
import com.emplk.realestatemanager.data.picture.PictureMapper
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.property.PropertyEntity
import com.emplk.realestatemanager.domain.property.PropertyRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
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

    override suspend fun add(propertyEntity: PropertyEntity): Long = withContext(coroutineDispatcherProvider.io) {
        try {
            val propertyDtoEntity = propertyMapper.mapToDtoEntity(propertyEntity)
            propertyDao.insert(propertyDtoEntity)
        } catch (e: SQLiteException) {
            e.printStackTrace()
            -1L
        }
    }

    override suspend fun addPropertyWithDetails(propertyEntity: PropertyEntity): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            val propertyIdDeferred: Deferred<Long> = async { add(propertyEntity) }
            val propertyId = propertyIdDeferred.await()

            val locationJob = launch {
                val locationDtoEntity = locationMapper.mapToDtoEntity(propertyEntity.location, propertyId)
                locationDao.insert(locationDtoEntity)
            }

            val picturesJob = launch {
                propertyEntity.pictures.map { pictureEntity ->
                    val pictureDtoEntity = pictureMapper.mapToDtoEntity(pictureEntity, propertyId)
                    pictureDao.insert(pictureDtoEntity)
                }
            }

            val amenitiesJob = launch {
                propertyEntity.amenities.map {
                    val amenityDtoEntity = amenityMapper.mapToDtoEntity(it, propertyId)
                    amenityDao.insert(amenityDtoEntity)
                }
            }

            // Wait for all child jobs to complete
            val childrenJobs = listOf(locationJob, picturesJob, amenitiesJob)
            childrenJobs.joinAll()
            true
        }


    override fun getPropertiesAsFlow(): Flow<List<PropertyEntity>> = propertyDao
        .getPropertiesWithDetailsFlow()
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
        .getPropertyById(propertyId)
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