package com.emplk.realestatemanager.data.property_form

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

@Dao
interface PropertyFormDao {
    @Insert
    suspend fun insert(propertyFormDto: PropertyFormDto): Long

    @Query("SELECT * FROM property_forms WHERE id = :propertyFormId LIMIT 1")
    suspend fun getPropertyForm(propertyFormId: Long): PropertyFormDto

    @Query("SELECT * FROM property_forms WHERE id = :propertyFormId LIMIT 1")
    suspend fun getPropertyFormWithDetailsAsFlow(propertyFormId: Long): Flow<PropertyFormWithDetails>

    @Query("UPDATE property_forms SET type = :newType WHERE id = :propertyFormId")
    suspend fun updateType(propertyFormId: Long, newType: String)

    @Query("UPDATE property_forms SET price = :newPrice WHERE id = :propertyFormId")
    suspend fun updatePrice(propertyFormId: Long, newPrice: BigDecimal)

    @Query("UPDATE property_forms SET surface = :newSurface WHERE id = :propertyFormId")
    suspend fun updateSurface(propertyFormId: Long, newSurface: Int)

    @Query("UPDATE property_forms SET rooms = :newRooms WHERE id = :propertyFormId")
    suspend fun updateRooms(propertyFormId: Long, newRooms: Int)

    @Query("UPDATE property_forms SET bedrooms = :newBedrooms WHERE id = :propertyFormId")
    suspend fun updateBedrooms(propertyFormId: Long, newBedrooms: Int)

    @Query("UPDATE property_forms SET bathrooms = :newBathrooms WHERE id = :propertyFormId")
    suspend fun updateBathrooms(propertyFormId: Long, newBathrooms: Int)

    @Query("UPDATE property_forms SET description = :newDescription WHERE id = :propertyFormId")
    suspend fun updateDescription(propertyFormId: Long, newDescription: String?)

    @Query("DELETE FROM property_forms WHERE id = :propertyFormId")
    suspend fun delete(propertyFormId: Long): Int
}