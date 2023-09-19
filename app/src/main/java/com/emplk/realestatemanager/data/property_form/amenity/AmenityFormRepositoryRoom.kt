package com.emplk.realestatemanager.data.property_form.amenity

import com.emplk.realestatemanager.domain.property_form.amenity.AmenityFormEntity
import com.emplk.realestatemanager.domain.property_form.amenity.AmenityFormRepository
import javax.inject.Inject

class AmenityFormRepositoryRoom @Inject constructor(
    private val amenityFormDao: AmenityFormDao,
    private val amenityFormMapper: AmenityFormMapper,
) : AmenityFormRepository {
    override suspend fun add(amenityFormEntity: AmenityFormEntity, propertyFormId: Long): Long =
        amenityFormDao.insert(
            amenityFormMapper.mapToAmenityDto(amenityFormEntity, propertyFormId)
        )

    override suspend fun delete(amenityFormId: Long): Int =
        amenityFormDao.delete(amenityFormId)
}
