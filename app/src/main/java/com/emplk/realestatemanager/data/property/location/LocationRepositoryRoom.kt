package com.emplk.realestatemanager.data.property.location

import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.property.location.LocationRepository
import com.emplk.realestatemanager.domain.property.location.PropertyLatLongAndSoldStatusEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationRepositoryRoom @Inject constructor(
    private val locationDao: LocationDao,
    private val locationMapper: LocationMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : LocationRepository {

    override fun getAllPropertyLatLongAndSoldStatusAsFlow(): Flow<List<PropertyLatLongAndSoldStatusEntity>> =
        locationDao
            .getAllPropertyLatLongAsFlow().map { propertyLatLongDtos ->
                propertyLatLongDtos.mapNotNull { propertyLatLongDto ->
                    locationMapper.mapToPropertyLatLongEntity(propertyLatLongDto)
                }
            }.flowOn(coroutineDispatcherProvider.io)
}