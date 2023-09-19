package com.emplk.realestatemanager.data.property_form.amenity

import com.emplk.realestatemanager.domain.property_form.amenity.AmenityFormEntity
import com.emplk.realestatemanager.domain.property_form.amenity.AmenityFormRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AmenityFormRepositoryRoom @Inject constructor(
    private val amenityFormDao: AmenityFormDao,
    private val amenityFormMapper: AmenityFormMapper,
) : AmenityFormRepository {
    override suspend fun add(amenityFormEntity: AmenityFormEntity): Long =
        amenityFormDao.insert(
            amenityFormMapper.mapToAmenityDto(amenityFormEntity)
        )

    override fun getAllAsFlow(): Flow<List<AmenityFormEntity>> =
        amenityFormDao.getAllAsFlow()
        .map { amenityFormDtos ->
            amenityFormDtos.map { amenityFormDto ->
                amenityFormMapper.mapToAmenityFormEntity(amenityFormDto, amenityFormDto.propertyFormId)
            }
        }

    override suspend fun delete(amenityFormId: Long): Int =
        amenityFormDao.delete(amenityFormId)
}
