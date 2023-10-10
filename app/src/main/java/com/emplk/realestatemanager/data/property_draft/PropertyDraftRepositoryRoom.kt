package com.emplk.realestatemanager.data.property_draft

import android.database.sqlite.SQLiteException
import android.util.Log
import com.emplk.realestatemanager.data.property_draft.amenity.AmenityDraftDao
import com.emplk.realestatemanager.data.property_draft.amenity.AmenityDraftMapper
import com.emplk.realestatemanager.data.property_draft.picture_preview.PicturePreviewDao
import com.emplk.realestatemanager.data.property_draft.picture_preview.PicturePreviewMapper
import com.emplk.realestatemanager.data.property_draft.picture_preview.PropertyDraftMapper
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.property_draft.PropertyDraftEntity
import com.emplk.realestatemanager.domain.property_draft.PropertyFormRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PropertyDraftRepositoryRoom @Inject constructor(
    private val propertyDraftDao: PropertyDraftDao,
    private val picturePreviewDao: PicturePreviewDao,
    private val amenityDraftDao: AmenityDraftDao,
    private val propertyDraftMapper: PropertyDraftMapper,
    private val amenityDraftMapper: AmenityDraftMapper,
    private val picturePreviewMapper: PicturePreviewMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : PropertyFormRepository {

    override suspend fun add(propertyDraftEntity: PropertyDraftEntity): Long? =
        withContext(coroutineDispatcherProvider.io) {
            try {
                propertyDraftDao.insert(propertyDraftMapper.mapToPropertyDraftDto(propertyDraftEntity))
            } catch (e: SQLiteException) {
                e.printStackTrace()
                null
            }
        }

    override suspend fun addPropertyFormWithDetails(propertyDraftEntity: PropertyDraftEntity): Long =
        withContext(coroutineDispatcherProvider.io) {
            try {
                val propertyFormId =
                    add(propertyDraftEntity) ?: return@withContext -1L // TODO: revoir ça et mettre du null

                val picturePreviewsFormAsync = propertyDraftEntity.pictures.map { picturePreviewEntity ->
                    async {
                        val picturePreviewFormDto =
                            picturePreviewMapper.mapToPicturePreviewDto(picturePreviewEntity, propertyFormId)
                        picturePreviewDao.insert(picturePreviewFormDto)
                    }
                }

                val amenitiesFormAsync = propertyDraftEntity.amenities.map {
                    async {
                        val amenityFormDto = amenityDraftMapper.mapToAmenityDto(it, propertyFormId)
                        amenityDraftDao.insert(amenityFormDto)
                    }
                }

                (picturePreviewsFormAsync + amenitiesFormAsync).all { it.await() != null }
                propertyFormId
            } catch (e: SQLiteException) {
                e.printStackTrace()
                -1L
            }
        }

    override suspend fun getExistingPropertyFormId(): Long? = withContext(coroutineDispatcherProvider.io) {
        try {
            propertyDraftDao.getExistingPropertyFormId()
        } catch (e: SQLiteException) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getExistingPropertyForm(): PropertyDraftEntity? = withContext(coroutineDispatcherProvider.io) {
        try {
            propertyDraftDao.getExistingPropertyForm()?.let { propertyFormWithDetails ->
                propertyDraftMapper.mapToPropertyDraftEntity(
                    propertyFormWithDetails.propertyForm,
                    propertyFormWithDetails.picturePreviews,
                    propertyFormWithDetails.amenities,
                )
            }
        } catch (e: SQLiteException) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getPropertyFormById(propertyFormId: Long): PropertyDraftEntity =
        withContext(coroutineDispatcherProvider.io) {
            propertyDraftDao.getPropertyFormById(propertyFormId).let { propertyFormWithDetails ->
                propertyDraftMapper.mapToPropertyDraftEntity(
                    propertyFormWithDetails.propertyForm,
                    propertyFormWithDetails.picturePreviews,
                    propertyFormWithDetails.amenities,
                )
            }
        }

    override suspend fun exists(): Boolean = withContext(coroutineDispatcherProvider.io) {
        propertyDraftDao.exists()
    }

    override suspend fun update(propertyDraftEntity: PropertyDraftEntity, propertyFormId: Long) =
        withContext(coroutineDispatcherProvider.io) {

            val propertyFormDto = propertyDraftMapper.mapToPropertyDraftDto(propertyDraftEntity)

            propertyDraftDao.update(
                propertyFormDto.type,
                propertyFormDto.price,
                propertyFormDto.surface,
                propertyFormDto.address,
                propertyFormDto.rooms,
                propertyFormDto.bedrooms,
                propertyFormDto.bathrooms,
                propertyFormDto.description,
                propertyFormDto.agentName,
                propertyFormId
            )


            val amenityIdsStoredInDb = amenityDraftDao.getAllIds(propertyFormId)

            propertyDraftEntity.amenities.forEach {
                val amenityFormDto = amenityDraftMapper.mapToAmenityDto(it, propertyFormId)
                if (!amenityIdsStoredInDb.contains(amenityFormDto.id)) {
                    amenityDraftDao.insert(amenityFormDto)
                }
            }

            amenityIdsStoredInDb.forEach { amenityIdStoredInDatabase ->
                if (propertyDraftEntity.amenities.none { it.id == amenityIdStoredInDatabase }) {
                    amenityDraftDao.delete(amenityIdStoredInDatabase)
                }
            }
        }

    override suspend fun delete(propertyFormId: Long): Boolean = withContext(coroutineDispatcherProvider.io) {
        try {
            val amenityDeletionAsync = async { amenityDraftDao.deleteAll(propertyFormId) }

            val picturePreviewDeletionAsync = async { picturePreviewDao.deleteAll(propertyFormId) }

            val propertyDeletionAsync = async { propertyDraftDao.delete(propertyFormId) }

            (listOf(propertyDeletionAsync) + amenityDeletionAsync + picturePreviewDeletionAsync)
                .all { it.await() != null }
        } catch (e: SQLiteException) {
            Log.d("COUCOU", "delete: ${e.message}")
            e.printStackTrace()
            false
        }
    }
}