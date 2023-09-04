package com.emplk.realestatemanager.data.property

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.emplk.realestatemanager.domain.location.LocationEntity
import com.emplk.realestatemanager.domain.property.PropertyWithDetailsEntity
import com.emplk.realestatemanager.domain.property.PropertyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Insert
    suspend fun insert(propertyEntity: PropertyEntity): Long

    @Transaction
    @Query("SELECT * FROM properties WHERE id = :propertyId")
    fun getPropertyById(propertyId: Long): Flow<PropertyWithDetailsEntity>

    @Query("SELECT uri FROM pictures WHERE property_id = :propertyId")
    fun getPhotosByPropertyId(propertyId: Long): Flow<List<String>>

    @Query("SELECT * FROM locations WHERE property_id = :propertyId")
    fun getLocations(propertyId: Long): Flow<LocationEntity>

    @Query("SELECT * FROM properties")
    fun getProperties(): Flow<List<PropertyEntity>>

    @Transaction
    @Query("SELECT * FROM properties")
    fun getPropertiesWithPicturesAndLocation(): Flow<List<PropertyWithDetailsEntity>>

    @Update
    suspend fun update(propertyEntity: PropertyEntity): Int
}