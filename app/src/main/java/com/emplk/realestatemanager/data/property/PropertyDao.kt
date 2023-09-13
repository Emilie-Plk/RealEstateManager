package com.emplk.realestatemanager.data.property

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Insert
    suspend fun insert(propertyDtoEntity: PropertyDtoEntity) : Long

    @Transaction
    @Query("SELECT * FROM properties WHERE id = :propertyId")
    fun getPropertyById(propertyId: Long): Flow<PropertyWithDetails>

    @Query("SELECT * FROM properties")
    fun getProperties(): Flow<List<PropertyDtoEntity>>

    @Transaction
    @Query("SELECT * FROM properties")
    fun getPropertiesWithDetailsFlow(): Flow<List<PropertyWithDetails>>

    @Update
    suspend fun update(propertyDtoEntity: PropertyDtoEntity): Int
}