package com.emplk.realestatemanager.data.property

import android.database.sqlite.SQLiteException
import com.emplk.realestatemanager.data.property.location.LocationDao
import com.emplk.realestatemanager.data.property.location.LocationMapper
import com.emplk.realestatemanager.data.property.picture.PictureDao
import com.emplk.realestatemanager.data.property.picture.PictureMapper
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.filter.PropertyMinMaxStatsEntity
import com.emplk.realestatemanager.domain.property.PropertyEntity
import com.emplk.realestatemanager.domain.property.PropertyRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import javax.inject.Inject

class PropertyRepositoryRoom @Inject constructor(
    private val propertyDao: PropertyDao,
    private val locationDao: LocationDao,
    private val pictureDao: PictureDao,
    private val propertyMapper: PropertyMapper,
    private val locationMapper: LocationMapper,
    private val pictureMapper: PictureMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : PropertyRepository {

    override suspend fun add(propertyEntity: PropertyEntity): Long? = withContext(coroutineDispatcherProvider.io) {
        try {
            propertyDao.insert(propertyMapper.mapToDto(propertyEntity))
        } catch (e: SQLiteException) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun addPropertyWithDetails(propertyEntity: PropertyEntity): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            val propertyId = add(propertyEntity) ?: return@withContext false

            val locationAsync = async {
                val locationDtoEntity = locationMapper.mapToDto(propertyEntity.location, propertyId)
                locationDao.insert(locationDtoEntity)
            }

            val picturesAsync = propertyEntity.pictures.map {
                async {
                    val pictureDtoEntity = pictureMapper.mapToDtoEntity(it, propertyId)
                    pictureDao.insert(pictureDtoEntity)
                }
            }

            (listOf(locationAsync) + picturesAsync).all { it.await() != null }
        }


    override fun getPropertiesAsFlow(): Flow<List<PropertyEntity>> = propertyDao
        .getPropertiesWithDetailsAsFlow()
        .map { propertyWithDetailsEntities ->
            propertyWithDetailsEntities.mapNotNull { propertyWithDetailsEntity ->
                propertyMapper.mapToDomainEntity(
                    propertyWithDetailsEntity.property,
                    propertyWithDetailsEntity.location ?: return@mapNotNull null,
                    propertyWithDetailsEntity.pictures,
                )
            }
        }
        .flowOn(coroutineDispatcherProvider.io)

    override fun getPropertiesCountAsFlow(): Flow<Int> = propertyDao.getPropertiesCountAsFlow()

    override fun getPropertyByIdAsFlow(propertyId: Long): Flow<PropertyEntity?> = propertyDao
        .getPropertyByIdAsFlow(propertyId)
        .map {
            it?.let { propertyWithDetailsEntity ->
                propertyMapper.mapToDomainEntity(
                    propertyWithDetailsEntity.property,
                    propertyWithDetailsEntity.location ?: return@map null,
                    propertyWithDetailsEntity.pictures,
                )
            }
        }
        .flowOn(coroutineDispatcherProvider.io)

    override suspend fun getPropertyById(propertyId: Long): PropertyEntity =
        withContext(coroutineDispatcherProvider.io) {
            propertyDao.getPropertyById(propertyId)?.let {
                propertyMapper.mapToDomainEntity(
                    it.property,
                    it.location ?: return@withContext null,
                    it.pictures,
                )
            }
        } ?: throw IllegalStateException("Property with id $propertyId not found")

    override suspend fun getMinMaxPricesAndSurfaces(): PropertyMinMaxStatsEntity =
        withContext(coroutineDispatcherProvider.io) {
            propertyDao.getMinMaxPricesAndSurfaces()
        }

    override fun getFilteredPropertiesCount(
        propertyType: String?,
        minPrice: BigDecimal,
        maxPrice: BigDecimal,
        minSurface: BigDecimal,
        maxSurface: BigDecimal,
        amenitySchool: Boolean?,
        amenityPark: Boolean?,
        amenityShopping: Boolean?,
        amenityRestaurant: Boolean?,
        amenityConcierge: Boolean?,
        amenityGym: Boolean?,
        amenityTransport: Boolean?,
        amenityHospital: Boolean?,
        amenityLibrary: Boolean?,
        entryDateEpochMin: Long?,
        entryDateEpochMax: Long?,
        isSold: Boolean?
    ): Flow<Int> = propertyDao.getFilteredPropertiesCount(
        propertyType,
        minPrice,
        maxPrice,
        minSurface,
        maxSurface,
        amenitySchool,
        amenityPark,
        amenityShopping,
        amenityRestaurant,
        amenityConcierge,
        amenityGym,
        amenityTransport,
        amenityHospital,
        amenityLibrary,
        entryDateEpochMin,
        entryDateEpochMax,
        isSold
    )


    override suspend fun update(propertyEntity: PropertyEntity): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            try {
                propertyDao.update(propertyMapper.mapToDto(propertyEntity))
                locationDao.update(locationMapper.mapToDto(propertyEntity.location, propertyEntity.id))
                propertyEntity.pictures.map {
                    pictureDao.upsert(pictureMapper.mapToDtoEntity(it, propertyEntity.id))
                }
                true
            } catch (e: SQLiteException) {
                e.printStackTrace()
                false
            }
        }
}