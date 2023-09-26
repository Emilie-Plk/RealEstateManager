package com.emplk.realestatemanager.data.property_form.amenity

import com.emplk.realestatemanager.domain.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.property_form.amenity.AmenityFormEntity
import com.emplk.realestatemanager.domain.property_form.amenity.AmenityFormRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AmenityFormRepositoryRoom @Inject constructor(
    private val amenityFormDao: AmenityFormDao,
    private val amenityFormMapper: AmenityFormMapper,
) : AmenityFormRepository {
    override suspend fun add(amenityEntity: AmenityEntity, propertyFormId: Long): Long? =
        amenityFormDao.insert(
            amenityFormMapper.mapToAmenityDto(amenityEntity, propertyFormId)
        )

    override suspend fun addAll(amenityEntities: List<AmenityEntity>, propertyFormId: Long): List<Long?> =
        amenityFormDao.insertAll(
            amenityEntities.map { amenityEntity ->
                amenityFormMapper.mapToAmenityDto(amenityEntity, propertyFormId)
            }
        )

    override fun getAllAsFlow(): Flow<List<AmenityEntity>> =
        amenityFormDao.getAllAsFlow()
            .map { amenityFormDtos ->
                amenityFormDtos.map { amenityFormDto ->
                    amenityFormMapper.mapToAmenityFormEntity(amenityFormDto)
                }
            }

    override suspend fun delete(amenityFormId: Long): Int =
        amenityFormDao.delete(amenityFormId)
}
