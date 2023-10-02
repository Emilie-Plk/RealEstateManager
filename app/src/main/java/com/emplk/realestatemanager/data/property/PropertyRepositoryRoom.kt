package com.emplk.realestatemanager.data.property

import android.database.sqlite.SQLiteException
import android.util.Log
import com.emplk.realestatemanager.data.property.amenity.AmenityDao
import com.emplk.realestatemanager.data.property.amenity.AmenityMapper
import com.emplk.realestatemanager.data.property.location.LocationDao
import com.emplk.realestatemanager.data.property.location.LocationMapper
import com.emplk.realestatemanager.data.property.picture.PictureDao
import com.emplk.realestatemanager.data.property.picture.PictureMapper
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.property.PropertyEntity
import com.emplk.realestatemanager.domain.property.PropertyRepository
import com.emplk.realestatemanager.domain.property.type_price_surface.PropertyTypePriceAndSurfaceEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PropertyRepositoryRoom @Inject constructor(
    private val propertyDao: PropertyDao,
    private val locationDao: LocationDao,
    private val pictureDao: PictureDao,
    private val amenityDao: AmenityDao,
    private val propertyMapper: PropertyMapper,
    private val propertyTypePriceAndSurfaceMapper: PropertyTypeSurfacePriceAndPictureDtoMapper,
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
            Log.d("COUCOU", "addPropertyWithDetails: $propertyId")

            val locationAsync = async {
                val locationDtoEntity = locationMapper.mapToDtoEntity(propertyEntity.location, propertyId)
                locationDao.insert(locationDtoEntity)
            }
            Log.d("COUCOU", "addPropertyWithDetails: $locationAsync")

            val picturesAsync = propertyEntity.pictures.map {
                async {
                    val pictureDtoEntity = pictureMapper.mapToDtoEntity(it, propertyId)
                    pictureDao.insert(pictureDtoEntity)
                }
            }
            Log.d("COUCOU", "addPropertyWithDetails: $picturesAsync")

            val amenitiesAsync = propertyEntity.amenities.map {
                async {
                    val amenityDtoEntity = amenityMapper.mapToDtoEntity(it, propertyId)
                    amenityDao.insert(amenityDtoEntity)
                    Log.d("COUCOU", "addPropertyWithDetails: $it")
                }
            }

            // Wait for all child jobs to complete
            (listOf(locationAsync) + picturesAsync + amenitiesAsync).all { it.await() != null }
        }


    override fun getPropertiesAsFlow(): Flow<List<PropertyEntity>> = propertyDao
        .getPropertiesWithDetailsAsFlow()
        .map { propertyWithDetailsEntities ->
            propertyWithDetailsEntities.mapNotNull { propertyWithDetailsEntity ->
                propertyMapper.mapToDomainEntity(
                    propertyWithDetailsEntity.property,
                    propertyWithDetailsEntity.location ?: return@mapNotNull null,
                    propertyWithDetailsEntity.pictures,
                    propertyWithDetailsEntity.amenities,
                )
            }
        }
        .flowOn(coroutineDispatcherProvider.io)

    override fun getPropertyByIdAsFlow(propertyId: Long): Flow<PropertyEntity> = propertyDao
        .getPropertyByIdAsFlow(propertyId)
        .mapNotNull {
            propertyMapper.mapToDomainEntity(
                it.property,
                it.location ?: return@mapNotNull null,
                it.pictures,
                it.amenities,
            )
        }
        .flowOn(coroutineDispatcherProvider.io)

    override suspend fun getPropertyTypeSurfaceAndPriceById(propertyId: Long): PropertyTypePriceAndSurfaceEntity =
        withContext(coroutineDispatcherProvider.io) {
            propertyTypePriceAndSurfaceMapper.toEntity(propertyDao.getPropertyTypePriceAndSurfaceById(propertyId))
        }

    override suspend fun update(propertyEntity: PropertyEntity): Int =
        withContext(coroutineDispatcherProvider.io) {
            propertyDao.update(propertyMapper.mapToDtoEntity(propertyEntity))
        }
}