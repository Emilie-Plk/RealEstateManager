package com.emplk.realestatemanager.data.property

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.emplk.realestatemanager.domain.entities.LocationEntity
import com.emplk.realestatemanager.domain.entities.PropertyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Insert
    suspend fun insert(propertyEntity: PropertyEntity)

    @Query("SELECT * FROM properties WHERE id = :propertyId")
    fun getPropertyById(propertyId: Long): Flow<PropertyEntity>

    @Transaction
    @Query("SELECT photos FROM properties WHERE id = :propertyId")
    fun getPhotosByPropertyId(propertyId: Long): Flow<List<String>>

    @Transaction
    @Query("SELECT location FROM properties WHERE id = :propertyId")
    fun getLocations(propertyId: Long): Flow<List<LocationEntity>>

    @Transaction
    @Query("SELECT * FROM properties")
    fun getProperties(): Flow<List<PropertyEntity>>

    @Update
    suspend fun update(propertyEntity: PropertyEntity): Int
}