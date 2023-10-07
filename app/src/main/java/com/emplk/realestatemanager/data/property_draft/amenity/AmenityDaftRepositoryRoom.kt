package com.emplk.realestatemanager.data.property_draft.amenity

import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.property_draft.amenity.AmenityFormRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AmenityDaftRepositoryRoom @Inject constructor(
    private val amenityDraftDao: AmenityDraftDao,
    private val amenityDraftMapper: AmenityDraftMapper,
) : AmenityFormRepository {
    override suspend fun add(amenityEntity: AmenityEntity, propertyFormId: Long): Long? =
        amenityDraftDao.insert(
            amenityDraftMapper.mapToAmenityDto(amenityEntity, propertyFormId)
        )

    override suspend fun addAll(amenityEntities: List<AmenityEntity>, propertyFormId: Long): List<Long?> =
        amenityDraftDao.insertAll(
            amenityEntities.map { amenityEntity ->
                amenityDraftMapper.mapToAmenityDto(amenityEntity, propertyFormId)
            }
        )

    override fun getAllAsFlow(): Flow<List<AmenityEntity>> =
        amenityDraftDao.getAllAsFlow()
            .map { amenityFormDtos ->
                amenityFormDtos.map { amenityFormDto ->
                    amenityDraftMapper.mapToAmenityFormEntity(amenityFormDto)
                }
            }

    override suspend fun delete(amenityFormId: Long): Int =
        amenityDraftDao.delete(amenityFormId)
}
