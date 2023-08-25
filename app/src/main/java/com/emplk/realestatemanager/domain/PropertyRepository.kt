package com.emplk.realestatemanager.domain

import com.emplk.realestatemanager.domain.entities.PropertiesWithPicturesAndLocationEntity
import com.emplk.realestatemanager.domain.entities.PropertyEntity
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    suspend fun add(propertyEntity: PropertyEntity)
    suspend fun update(propertyEntity: PropertyEntity): Int
    fun getPropertiesAsFlow(): Flow<List<PropertiesWithPicturesAndLocationEntity>>
    fun getPropertyByIdAsFlow(propertyId: Long): Flow<PropertiesWithPicturesAndLocationEntity>
}
