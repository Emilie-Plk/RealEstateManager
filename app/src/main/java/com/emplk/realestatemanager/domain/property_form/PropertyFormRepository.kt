package com.emplk.realestatemanager.domain.property_form

import java.math.BigDecimal

interface PropertyFormRepository {
    suspend fun add(propertyFormEntity: PropertyFormEntity): Long
    suspend fun updateType(pictureId: Long, newType: String)

    suspend fun updatePrice(pictureId: Long, newPrice: BigDecimal)

    suspend fun updateSurface(pictureId: Long, newSurface: Int)

    suspend fun updateRooms(pictureId: Long, newRooms: Int)

    suspend fun updateBedrooms(pictureId: Long, newBedrooms: Int)

    suspend fun updateBathrooms(pictureId: Long, newBathrooms: Int)

    suspend fun updateDescription(pictureId: Long, newDescription: String?)
}
