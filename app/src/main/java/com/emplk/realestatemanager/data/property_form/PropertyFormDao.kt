package com.emplk.realestatemanager.data.property_form

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

@Dao
interface PropertyFormDao {
    @Insert
    suspend fun insert(propertyFormDto: PropertyFormDto): Long

    @Transaction
    @Query("SELECT * FROM property_forms WHERE id = :propertyFormId")
    fun getPropertyFormById(propertyFormId: Long): Flow<PropertyFormWithDetails>

    @Query("SELECT EXISTS(SELECT 1 FROM property_forms)")
    suspend fun exists(): Boolean

    @Query("SELECT id FROM property_forms LIMIT 1")
    suspend fun getExistingPropertyFormId(): Long

    @Query(
        "UPDATE property_forms SET type = :newType," +
                " price = :newPrice," +
                " surface = :newSurface," +
                " rooms = :newRooms," +
                " bedrooms = :newBedrooms," +
                " bathrooms = :newBathrooms," +
                " description = :newDescription," +
                " agentName = :newAgentName WHERE id = :propertyFormId"
    )
    suspend fun update(
        newType: String?,
        newPrice: BigDecimal?,
        newSurface: Int?,
        newRooms: Int?,
        newBedrooms: Int?,
        newBathrooms: Int?,
        newDescription: String?,
        newAgentName: String?,
        propertyFormId: Long
    ): Int

    @Query("DELETE FROM property_forms WHERE id = :propertyFormId")
    suspend fun delete(propertyFormId: Long)
}