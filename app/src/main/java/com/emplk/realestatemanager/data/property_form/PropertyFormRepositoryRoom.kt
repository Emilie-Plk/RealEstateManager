package com.emplk.realestatemanager.data.property_form

import android.database.sqlite.SQLiteException
import com.emplk.realestatemanager.data.property_form.amenity.AmenityFormDao
import com.emplk.realestatemanager.data.property_form.amenity.AmenityFormMapper
import com.emplk.realestatemanager.data.property_form.location.LocationFormDao
import com.emplk.realestatemanager.data.property_form.location.LocationFormMapper
import com.emplk.realestatemanager.data.property_form.picture_preview.PicturePreviewDao
import com.emplk.realestatemanager.data.property_form.picture_preview.PicturePreviewMapper
import com.emplk.realestatemanager.data.property_form.picture_preview.PropertyFormMapper
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.property_form.PropertyFormEntity
import com.emplk.realestatemanager.domain.property_form.PropertyFormRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PropertyFormRepositoryRoom @Inject constructor(
    private val propertyFormDao: PropertyFormDao,
    private val locationFormDao: LocationFormDao,
    private val picturePreviewDao: PicturePreviewDao,
    private val amenityFormDao: AmenityFormDao,
    private val propertyFormMapper: PropertyFormMapper,
    private val amenityFormMapper: AmenityFormMapper,
    private val locationFormMapper: LocationFormMapper,
    private val picturePreviewMapper: PicturePreviewMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : PropertyFormRepository {

    private val isPropertyFormInProgressMutableStateFlow = MutableStateFlow(false)

    override suspend fun add(propertyFormEntity: PropertyFormEntity): Long? =
        withContext(coroutineDispatcherProvider.io) {
            try {
                propertyFormDao.insert(propertyFormMapper.mapToPropertyFormDto(propertyFormEntity))
            } catch (e: SQLiteException) {
                e.printStackTrace()
                null
            }
        }

    override suspend fun addPropertyFormWithDetails(propertyFormEntity: PropertyFormEntity): Long =
        withContext(coroutineDispatcherProvider.io) {
            val propertyFormId = add(propertyFormEntity) ?: return@withContext -1L

            val locationFormAsync = async {
                val locationFormDto = locationFormMapper.mapToLocationDto(propertyFormEntity.location, propertyFormId)
                locationFormDao.insert(locationFormDto)
            }
            val picturePreviewsFormAsync = propertyFormEntity.pictures.map { picturePreviewEntity ->
                async {
                    val picturePreviewFormDto =
                        picturePreviewMapper.mapToPicturePreviewDto(picturePreviewEntity, propertyFormId)
                    picturePreviewDao.insert(picturePreviewFormDto)
                }
            }

            val amenitiesFormAsync = propertyFormEntity.amenities.map {
                async {
                    val amenityFormDto = amenityFormMapper.mapToAmenityDto(it, propertyFormId)
                    amenityFormDao.insert(amenityFormDto)
                }
            }

            (listOf(locationFormAsync) + picturePreviewsFormAsync + amenitiesFormAsync).all { it.await() != null }
            propertyFormId
        }

    override fun setPropertyFormProgress(isPropertyFormInProgress: Boolean) {
        isPropertyFormInProgressMutableStateFlow.tryEmit(isPropertyFormInProgress)
    }

    override fun isPropertyFormInProgressAsFlow(): Flow<Boolean> =
        isPropertyFormInProgressMutableStateFlow.asStateFlow()

    override suspend fun getPropertyFormByIdAsFlow(propertyFormId: Long): Flow<PropertyFormEntity> =
        propertyFormDao.getPropertyFormById(propertyFormId).map { propertyFormWithDetails ->
            propertyFormMapper.mapToPropertyFormEntity(
                propertyFormWithDetails.propertyForm,
                propertyFormWithDetails.location,
                propertyFormWithDetails.picturePreviews,
                propertyFormWithDetails.amenities,
            )
        }.flowOn(coroutineDispatcherProvider.io)

    override suspend fun exists(): Boolean = withContext(coroutineDispatcherProvider.io) {
        propertyFormDao.exists()
    }

    override suspend fun update(propertyFormEntity: PropertyFormEntity) =
        withContext(coroutineDispatcherProvider.io) {
            propertyFormDao.update(propertyFormMapper.mapToPropertyFormDto(propertyFormEntity))

            locationFormDao.update(
                locationFormMapper.mapToLocationDto(
                    propertyFormEntity.location,
                    propertyFormEntity.id
                )
            )

            propertyFormEntity.pictures.forEach {
                picturePreviewDao.update(picturePreviewMapper.mapToPicturePreviewDto(it, propertyFormEntity.id))
            }
            propertyFormEntity.amenities.forEach {
                amenityFormDao.update(amenityFormMapper.mapToAmenityDto(it, propertyFormEntity.id))
            }
        }

    override suspend fun delete(propertyFormId: Long) = withContext(coroutineDispatcherProvider.io) {
        amenityFormDao.deleteAll(propertyFormId)
        picturePreviewDao.deleteAll(propertyFormId)
        locationFormDao.delete(propertyFormId)
        propertyFormDao.delete(propertyFormId)
    }
}