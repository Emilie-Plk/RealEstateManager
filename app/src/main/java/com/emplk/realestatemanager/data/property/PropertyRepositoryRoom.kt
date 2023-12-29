package com.emplk.realestatemanager.data.property

import android.database.sqlite.SQLiteException
import androidx.sqlite.db.SimpleSQLiteQuery
import com.emplk.realestatemanager.data.property.location.LocationDao
import com.emplk.realestatemanager.data.property.location.LocationMapper
import com.emplk.realestatemanager.data.property.picture.PictureDao
import com.emplk.realestatemanager.data.property.picture.PictureMapper
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.filter.PropertyMinMaxStatsEntity
import com.emplk.realestatemanager.domain.filter.model.SearchEntity
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

            val locationDeferred = async {
                val locationDtoEntity = locationMapper.mapToDto(propertyEntity.location, propertyId)
                locationDao.insert(locationDtoEntity)
            }

            val picturesDeferred = propertyEntity.pictures.map {
                async {
                    val pictureDtoEntity = pictureMapper.mapToDtoEntity(it, propertyId)
                    pictureDao.insert(pictureDtoEntity)
                }
            }

            (listOf(locationDeferred) + picturesDeferred).all { it.await() != null }
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

    override fun getFilteredPropertiesCountRawQuery(searchEntity: SearchEntity): Flow<Int> {
        val queryStringBuilder = StringBuilder("SELECT COUNT(*) FROM properties")
        val args = mutableListOf<Any>()

        val whereClauses = mutableListOf<String>()

        searchEntity.propertyType?.let {
            whereClauses.add("type = ?")
            args.add(it)
        }

        if (!searchEntity.minPrice.toDouble().equals(0.0) && !searchEntity.maxPrice.toDouble().equals(0.0)) { // range
            whereClauses.add("price BETWEEN ? AND ?")
            args.add(searchEntity.minPrice.toDouble())
            args.add(searchEntity.maxPrice.toDouble())
        } else if (!searchEntity.minPrice.toDouble().equals(0.0) && searchEntity.maxPrice.toDouble().equals(0.0)) {
            whereClauses.add("price >= ?")
            args.add(searchEntity.minPrice.toDouble())
        } else if (!searchEntity.maxPrice.toDouble().equals(0.0) && searchEntity.minPrice.toDouble().equals(0.0)) {
            whereClauses.add("price <= ?")
            args.add(searchEntity.maxPrice.toDouble())
        }

        if (!searchEntity.minSurface.toDouble().equals(0.0) && !searchEntity.maxSurface.toDouble().equals(0.0)) {
            whereClauses.add("surface BETWEEN ? AND ?")
            args.add(searchEntity.minSurface.toDouble())
            args.add(searchEntity.maxSurface.toDouble())
        } else if (!searchEntity.minSurface.toDouble().equals(0.0) && searchEntity.maxSurface.toDouble().equals(0.0)) {
            whereClauses.add("surface >= ?")
            args.add(searchEntity.minSurface.toDouble())
        } else if (!searchEntity.maxSurface.toDouble().equals(0.0) && searchEntity.minSurface.toDouble().equals(0.0)) {
            whereClauses.add("surface <= ?")
            args.add(searchEntity.maxSurface.toDouble())
        }

        if (searchEntity.amenitySchool == true) {
            whereClauses.add("amenity_school = 1")
        }
        if (searchEntity.amenityPark == true) {
            whereClauses.add("amenity_park = 1")
        }
        if (searchEntity.amenityShopping == true) {
            whereClauses.add("amenity_shopping = 1")
        }
        if (searchEntity.amenityRestaurant == true) {
            whereClauses.add("amenity_restaurant = 1")
        }
        if (searchEntity.amenityConcierge == true) {
            whereClauses.add("amenity_concierge = 1")
        }
        if (searchEntity.amenityGym == true) {
            whereClauses.add("amenity_gym = 1")
        }
        if (searchEntity.amenityTransport == true) {
            whereClauses.add("amenity_transportation = 1")
        }
        if (searchEntity.amenityHospital == true) {
            whereClauses.add("amenity_hospital = 1")
        }
        if (searchEntity.amenityLibrary == true) {
            whereClauses.add("amenity_library = 1")
        }

        if (searchEntity.entryDateEpochMin != null && searchEntity.entryDateEpochMax != null) {
            whereClauses.add("entry_date_epoch BETWEEN ? AND ?")
            args.add(searchEntity.entryDateEpochMin)
            args.add(searchEntity.entryDateEpochMax)
        }

        if (searchEntity.isSold != null) {
            whereClauses.add("(:isSold = 0 AND sale_date IS NULL) OR (:isSold = 1 AND sale_date IS NOT NULL)")
            args.add(searchEntity.isSold)
        }

        if (whereClauses.isNotEmpty()) {
            queryStringBuilder.append(" WHERE ")
            queryStringBuilder.append(whereClauses.joinToString(" AND "))
        }

        val query = SimpleSQLiteQuery(queryStringBuilder.toString(), args.toTypedArray())
        return propertyDao.getFilteredPropertiesCountRawQuery(query)
    }

    override suspend fun update(propertyEntity: PropertyEntity): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            try {
                propertyDao.update(propertyMapper.mapToDto(propertyEntity))

                locationDao.update(
                    propertyEntity.location.address,
                    propertyEntity.location.miniatureMapUrl,
                    propertyEntity.location.latLng?.latitude,
                    propertyEntity.location.latLng?.longitude,
                    propertyEntity.id
                )

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