package com.emplk.realestatemanager.data.property

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.emplk.realestatemanager.data.location.LocationDtoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Insert
    suspend fun insert(propertyDtoEntity: PropertyDtoEntity): Long

    @Transaction
    @Query("SELECT * FROM properties WHERE id = :propertyId")
    fun getPropertyById(propertyId: Long): Flow<PropertyWithDetailsEntity>

    @Query("SELECT * FROM properties")
    fun getProperties(): Flow<List<PropertyDtoEntity>>

    @Transaction
    @Query("SELECT * FROM properties")
    fun getPropertiesWithDetailsFlow(): Flow<List<PropertyWithDetailsEntity>>

    @Update
    suspend fun update(propertyDtoEntity: PropertyDtoEntity): Int
}