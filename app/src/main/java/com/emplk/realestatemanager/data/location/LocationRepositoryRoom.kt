package com.emplk.realestatemanager.data.location

import com.emplk.realestatemanager.domain.entities.LocationEntity
import com.emplk.realestatemanager.domain.location.LocationRepository
import javax.inject.Inject

class LocationRepositoryRoom @Inject constructor(
    private val locationDao: LocationDao,
) : LocationRepository {

    override suspend fun addLocation(location: LocationEntity) {
        locationDao.insert(location)
    }

    override suspend fun updateLocation(locationEntity: LocationEntity) {
        locationDao.update(locationEntity)
    }

}